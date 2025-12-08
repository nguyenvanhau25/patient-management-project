package com.pm.billingservice.infrastructure.grpc;

import billing.BillingResponse;
import billing.BillingServiceGrpc;
import billing.DeleteBillingRequest;
import billing.DeleteBillingResponse;
import com.pm.billingservice.application.service.BillingAccountService;
import com.pm.billingservice.domain.BillingAccount;
import io.grpc.stub.StreamObserver;
import lombok.RequiredArgsConstructor;
import net.devh.boot.grpc.server.service.GrpcService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@GrpcService
@RequiredArgsConstructor
public class BillingGrpcService extends BillingServiceGrpc.BillingServiceImplBase {
    private static final Logger log = LoggerFactory.getLogger(
            BillingGrpcService.class);
    private final BillingAccountService billingAccountService;

    //Đây là RPC method mà server sẽ nhận request từ client.
    @Override
    public void createBillingAccount(billing.BillingRequest request,
                                     StreamObserver<billing.BillingResponse> responseObserver) {
        log.info("createBillingAccount request received {}",request.toString());
try {
    // gRPC chỉ nhận request, gọi service
    BillingAccount account = billingAccountService.createBillingAccount(
            request.getPatientId(),
            request.getName(),
            request.getEmail()
    );

    // Trả về BillingResponse
    BillingResponse response = BillingResponse.newBuilder()
            .setAccountId(account.getId().toString())
            .setStatus("CREATED")
            .build();

// gửi message response đến client.
    responseObserver.onNext(response);
    responseObserver.onCompleted();
} catch (Exception e) {
    BillingResponse errorResponse = BillingResponse.newBuilder()
            .setAccountId("")
            .setStatus("FAILED")
            .build();
    responseObserver.onNext(errorResponse);
    responseObserver.onCompleted();
}

    }
    @Override
    public void deleteBillingAccount(DeleteBillingRequest request, StreamObserver<DeleteBillingResponse> responseObserver) {
        log.info("deleteBillingAccount request received {}",request.toString());
        try{
            // tìm tài khoản và xóa theo patient id
            BillingAccount deleted = billingAccountService.deleteByPatientId(request.getPatientId());
            if(deleted == null){
                DeleteBillingResponse response = DeleteBillingResponse.newBuilder()
                        .setAccountId("")
                        .setStatus("Not Found")
                        .build();
                responseObserver.onNext(response);
            }else{
                DeleteBillingResponse response = DeleteBillingResponse.newBuilder()
                        .setAccountId(deleted.getId().toString())
                        .setStatus("Delete Success")
                        .build();
                responseObserver.onNext(response);
            }
        }catch(Exception e){
            log.error("deleteBillingAccount error",e);
            DeleteBillingResponse error = DeleteBillingResponse.newBuilder()
                    .setAccountId("")
                    .setStatus("FAILED")
                    .build();

            responseObserver.onNext(error);
        }
        responseObserver.onCompleted();
    }
}
