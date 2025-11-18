package com.pm.apigateway.infrastructure;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.reactive.function.client.WebClientResponseException;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

@RestControllerAdvice
public class JwtValidationException {
    @ExceptionHandler(WebClientResponseException.class)
    public Mono<Void> handleUnauthorizedException(ServerWebExchange serverWebExchange){
        serverWebExchange.getResponse().setStatusCode(HttpStatus.UNAUTHORIZED);
        return serverWebExchange.getResponse().setComplete();
    }
    //bắt tất cả exception của kiểu WebClientResponseException và trả về HTTP 401 Unauthorized
    //mono đại diện cho một giá trị duy nhất hoặc không có giá trị trong tương lai
}
