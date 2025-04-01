package com.pm.billingservice.grpc;

import billing.BillingServiceGrpc;
import com.pm.billingservice.dto.BillRequestDTO;
import com.pm.billingservice.model.Bill;
import com.pm.billingservice.service.BillingService;
import io.grpc.stub.StreamObserver;
import net.devh.boot.grpc.server.service.GrpcService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@GrpcService
public class BillingGrpcService  extends BillingServiceGrpc.BillingServiceImplBase {

    private static final Logger log = LoggerFactory.getLogger(BillingGrpcService.class);
    private final BillingService billingService;

    public BillingGrpcService (BillingService billingService) {
        this.billingService = billingService;
    }

    @Override
    public void createBilling (billing.BillingRequest billingRequest , StreamObserver<billing.BillingResponse> streamObserver) {
        log.info("CreateBilling request has been received! {}",billingRequest.toString());

        Bill bill = billingService.createBill(billingRequest);

        // Billing Response for gRPC
        billing.BillingResponse billingResponse = billing.BillingResponse.newBuilder()
                .setId(bill.getId().toString())
                .setIssueDate(bill.getIssueDate().toString())
                .setDueDate(bill.getDueDate().toString())
                .setStatus("ACTIVE")
                .build();

        streamObserver.onNext(billingResponse);
        streamObserver.onCompleted();

    }
}
