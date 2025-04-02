package com.pm.customerservice.proto;

import io.grpc.ManagedChannel;
import io.grpc.ManagedChannelBuilder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
public class BillingGrpcServiceClient {
    private static final Logger log = LoggerFactory.getLogger(BillingGrpcServiceClient.class);
    private final billing.BillingServiceGrpc.BillingServiceBlockingStub blockingStub;

    public BillingGrpcServiceClient (@Value ("${billing.service.address}")String serverAddress, @Value("${billing.service.grpc.port}") int serverPort) {
    log.info("Billing service channel is being initialized at {}:{}",serverAddress,serverPort);
        ManagedChannel channel = ManagedChannelBuilder.forAddress(serverAddress,serverPort).usePlaintext().build();
        blockingStub = billing.BillingServiceGrpc.newBlockingStub(channel);
    }

    public billing.BillingResponse createBilling(String customerId, String amount) {
        billing.BillingRequest billingRequest = billing.BillingRequest.newBuilder().setAmount(amount).setCustomerId(customerId).build();
        billing.BillingResponse billingResponse = blockingStub.createBilling(billingRequest);
        log.info("Received billing response from gRPC service: {}",billingResponse );

        return  billingResponse;
    }

    public billing.BillingResponse getBillsById (String customerId) {
        billing.GetBillsByIdRequest getBillsByIdRequest = billing.GetBillsByIdRequest.newBuilder().setCustomerId(customerId).build();
        billing.BillingResponse billingResponse = blockingStub.getBillsById(getBillsByIdRequest);
    }


}
