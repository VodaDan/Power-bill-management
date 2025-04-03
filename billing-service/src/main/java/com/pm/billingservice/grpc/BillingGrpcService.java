package com.pm.billingservice.grpc;

import billing.*;
import billing.BillingResponseList;
import billing.BillingServiceGrpc;
import com.pm.billingservice.dto.BillRequestDTO;
import com.pm.billingservice.dto.BillResponseDTO;
import com.pm.billingservice.mapper.BillMapper;
import com.pm.billingservice.model.Bill;
import com.pm.billingservice.service.BillingService;
import io.grpc.stub.StreamObserver;
import net.devh.boot.grpc.server.service.GrpcService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.List;

@GrpcService
public class BillingGrpcService  extends BillingServiceGrpc.BillingServiceImplBase {

    private static final Logger log = LoggerFactory.getLogger(BillingGrpcService.class);
    private final BillingService billingService;

    public BillingGrpcService (BillingService billingService) {
        this.billingService = billingService;
    }

    @Override
    public void createBilling (BillingRequest billingRequest , StreamObserver<BillingResponse> streamObserver) {
        log.info("CreateBilling request has been received! {}",billingRequest.toString());

        Bill bill = billingService.createBill(billingRequest);

        BillingResponse billingResponse = BillingResponse.newBuilder()
                .setId(bill.getId().toString())
                .setIssueDate(bill.getIssueDate().toString())
                .setDueDate(bill.getDueDate().toString())
                .setStatus("ACTIVE")
                .build();

        streamObserver.onNext(billingResponse);
        streamObserver.onCompleted();
    }


    @Override
    public void getBillsById(GetBillsByIdRequest billsByIdRequest, StreamObserver<BillingResponseList> streamObserver) {
        List<BillingResponse> bills = new ArrayList<>();
        if (!bills.isEmpty()) {
            for (Bill bill : billingService.getBills(billsByIdRequest)) {
                BillingResponse response = BillingResponse.newBuilder()
                        .setId(bill.getId().toString())
                        .setDueDate(bill.getDueDate().toString())
                        .setIssueDate(bill.getIssueDate().toString())
                        .setAmount(String.valueOf(bill.getAmount()))
                        .setStatus("ACTIVE")
                        .build();

                log.info("New Response:{}", response);
                bills.add(response);
            }
        } else {
            log.warn("No bills for customerId:{} found", String.valueOf(billsByIdRequest.getCustomerId()));
        }
        BillingResponseList responseList = BillingResponseList.newBuilder()
                .addAllBills(bills)
                .build();

        log.info("Bills sent : {}", responseList);
        streamObserver.onNext(responseList);
        streamObserver.onCompleted();
    }

    @Override
    public void deteleBillById(DeleteBillByIdRequest request, StreamObserver<DeleteBillResponse> streamObserver) {

        if(billingService.deleteBill(request)){
            DeleteBillResponse response = DeleteBillResponse.newBuilder()
                    .setStatus(true)
                    .setMessage("Bill with id: " + String.valueOf(request.getId()) + " deleted successfully!")
                    .build();
            streamObserver.onNext(response);
            streamObserver.onCompleted();
        } else {
            DeleteBillResponse response = DeleteBillResponse.newBuilder()
                    .setStatus(true)
                    .setMessage("Bill with id: " + String.valueOf(request.getId()) + " could not be deleted!")
                    .build();
            streamObserver.onNext(response);
            streamObserver.onCompleted();
        }
    }











}
