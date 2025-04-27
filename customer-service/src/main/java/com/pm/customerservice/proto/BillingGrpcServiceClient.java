package com.pm.customerservice.proto;
import billing.*;
import billing.BillingServiceGrpc;

import io.grpc.ManagedChannel;
import io.grpc.ManagedChannelBuilder;
import io.grpc.stub.StreamObserver;
import net.devh.boot.grpc.server.service.GrpcService;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
public class BillingGrpcServiceClient {
    private static final Logger log = LoggerFactory.getLogger(BillingGrpcServiceClient.class);
    private final BillingServiceGrpc.BillingServiceBlockingStub blockingStub;

    @Autowired
    public BillingGrpcServiceClient (@Value ("${billing.service.address}")String serverAddress, @Value("${billing.service.grpc.port}") int serverPort) {
    log.info("Billing service channel is being initialized at {}:{}",serverAddress,serverPort);
        ManagedChannel channel = ManagedChannelBuilder.forAddress(serverAddress,serverPort).usePlaintext().build();
        blockingStub = billing.BillingServiceGrpc.newBlockingStub(channel);
    }

    // Test constructor

    public BillingGrpcServiceClient(BillingServiceGrpc.BillingServiceBlockingStub blockingStub) {
        this.blockingStub = blockingStub;
    }

    public BillingResponse createBilling(String customerId, String amount) {
        BillingRequest billingRequest = BillingRequest.newBuilder().setAmount(amount).setCustomerId(customerId).build();
        BillingResponse billingResponse = blockingStub.createBilling(billingRequest);
        log.info("Received billing response from gRPC service: {}",billingResponse );

        return  billingResponse;
    }

    public BillingResponseList getBillsById(String customerId) {

        GetBillsByIdRequest getBillsByIdRequest = GetBillsByIdRequest.newBuilder().setCustomerId(customerId).build();

        BillingResponseList responseList = blockingStub.getBillsById(getBillsByIdRequest);

        // Iterate over the list of BillingResponse and log each bill
        for (BillingResponse bill : responseList.getBillsList()) {
            log.info("Received bill with ID: {}, Issue Date: {}, Due Date: {}, Status: {}",
                    bill.getId(), bill.getIssueDate(), bill.getDueDate(), bill.getStatus());
        }

        // Return the response list
        return responseList;
    }

    public DeleteBillResponse deleteBillById (String id) {
        DeleteBillByIdRequest request = DeleteBillByIdRequest.newBuilder().setId(id).build();

        DeleteBillResponse response = blockingStub.deteleBillById(request);

        log.info("Response recevied : {}", response);
        return response;
    }


}
