package com.pm.patientservice.application.grpc;

import billing.*;
import io.grpc.ManagedChannel;
import io.grpc.ManagedChannelBuilder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
public class BillingServiceGrpcClient {

  private static final Logger log = LoggerFactory.getLogger(
      BillingServiceGrpcClient.class);
  private final BillingServiceGrpc.BillingServiceBlockingStub blockingStub;

  public BillingServiceGrpcClient(
      @Value("${billing.service.address:localhost}") String serverAddress,
      @Value("${billing.service.grpc.port:9001}") int serverPort) {

    log.info("Connecting to Billing Service GRPC service at {}:{}",
        serverAddress, serverPort);
// kênh kết nối tới grpc server
    ManagedChannel channel = ManagedChannelBuilder.forAddress(serverAddress,
        serverPort).usePlaintext().build();

    blockingStub = BillingServiceGrpc.newBlockingStub(channel);
  }
// hàm này để dùng ở tầng patient service
  public BillingResponse createBillingAccount(String patientId, String name,
      String email) {

    BillingRequest request = BillingRequest.newBuilder()
            .setPatientId(patientId)
            .setName(name)
            .setEmail(email).build();
// gửi request đến server gRPC.
    BillingResponse response = blockingStub.createBillingAccount(request);
    log.info("Received response from billing service via GRPC: {}", response);
    return response;
  }

  public DeleteBillingResponse deleteBillingAccount(String patientId) {

    DeleteBillingRequest request = DeleteBillingRequest.newBuilder()
            .setPatientId(patientId)
            .build();

    log.info("Sending gRPC request to delete BillingAccount of patient {}", patientId);

    DeleteBillingResponse response = blockingStub.deleteBillingAccount(request);

    log.info("Billing service gRPC delete response: {}", response);

    return response;
  }
}
