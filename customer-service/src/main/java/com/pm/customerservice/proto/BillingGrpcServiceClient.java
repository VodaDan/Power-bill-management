package com.pm.customerservice.proto;
import billing.*;
import billing.BillingServiceGrpc;

import io.grpc.ManagedChannel;
import io.grpc.ManagedChannelBuilder;
import io.grpc.stub.StreamObserver;
import net.devh.boot.grpc.server.service.GrpcService;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
public class BillingGrpcServiceClient {
    private static final Logger log = LoggerFactory.getLogger(BillingGrpcServiceClient.class);
    private final BillingServiceGrpc.BillingServiceBlockingStub blockingStub;

    public BillingGrpcServiceClient (@Value ("${billing.service.address}")String serverAddress, @Value("${billing.service.grpc.port}") int serverPort) {
    log.info("Billing service channel is being initialized at {}:{}",serverAddress,serverPort);
        ManagedChannel channel = ManagedChannelBuilder.forAddress(serverAddress,serverPort).usePlaintext().build();
        blockingStub = billing.BillingServiceGrpc.newBlockingStub(channel);
    }

    public BillingResponse createBilling(String customerId, String amount) {
        BillingRequest billingRequest = BillingRequest.newBuilder().setAmount(amount).setCustomerId(customerId).build();
        BillingResponse billingResponse = blockingStub.createBilling(billingRequest);
        log.info("Received billing response from gRPC service: {}",billingResponse );

        return  billingResponse;
    }

    public billing.BillingResponseList getBillsById(String customerId) {
        // Create the request
        billing.GetBillsByIdRequest getBillsByIdRequest = billing.GetBillsByIdRequest.newBuilder().setCustomerId(customerId).build();

        // Call the gRPC method to get the response
        billing.BillingResponseList responseList = blockingStub.getBillsById(getBillsByIdRequest);

        // Iterate over the list of BillingResponse and log each bill (optional)
        for (billing.BillingResponse bill : responseList.getBillsList()) {
            log.info("Received bill with ID: {}, Issue Date: {}, Due Date: {}, Status: {}",
                    bill.getId(), bill.getIssueDate(), bill.getDueDate(), bill.getStatus());
        }

        // Return the response list
        return responseList;
    }


}
