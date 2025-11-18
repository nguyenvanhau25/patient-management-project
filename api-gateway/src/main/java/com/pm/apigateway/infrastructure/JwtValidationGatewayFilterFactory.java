package com.pm.apigateway.infrastructure;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.gateway.filter.GatewayFilter;
import org.springframework.cloud.gateway.filter.factory.AbstractGatewayFilterFactory;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;

@Component
public class JwtValidationGatewayFilterFactory
        extends AbstractGatewayFilterFactory <Object>{
    private final WebClient webClient;

    // gọi đến auth service để xác thực token
    public JwtValidationGatewayFilterFactory(WebClient.Builder webClientBuilder,
                                             @Value("${auth.service.url}") String authServiceUrl) {
        this.webClient = webClientBuilder.baseUrl(authServiceUrl).build();
    }
// giúp chuyển tiếp tự động tới patient service
    @Override
    public GatewayFilter apply(Object config) {
// exchange : đại diện cho http hiện tại
        return (exchange, chain) -> {
            // lấy token từ header
            String token =
                    exchange.getRequest().getHeaders().getFirst(HttpHeaders.AUTHORIZATION);

            if(token == null || !token.startsWith("Bearer ")) {
                exchange.getResponse().setStatusCode(HttpStatus.UNAUTHORIZED);
                return exchange.getResponse().setComplete();
            }

            // gọi sang auth để xác thực token
            return webClient.get()
                    .uri("/validate")
                    .header(HttpHeaders.AUTHORIZATION, token)
                    .retrieve()
                    .bodyToMono(RoleResponse.class) // <- nhận JSON có field role
                    .flatMap(roleResponse -> {
                        if (roleResponse == null
                                || roleResponse.getRole() == null
                                || roleResponse.getRole().isEmpty()
                                || roleResponse.getStatus() != 200) {
                            exchange.getResponse().setStatusCode(HttpStatus.UNAUTHORIZED);
                            return exchange.getResponse().setComplete();
                        }

                        var mutatedRequest = exchange.getRequest().mutate()
                                .header("X-Role", roleResponse.getRole())
                                .build();

                        var mutatedExchange = exchange.mutate()
                                .request(mutatedRequest)
                                .build();

                        return chain.filter(mutatedExchange);
                    })
                    .onErrorResume(error -> {
                        exchange.getResponse().setStatusCode(HttpStatus.UNAUTHORIZED);
                        return exchange.getResponse().setComplete();
                    });
        };
    }
}
