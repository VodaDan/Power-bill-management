package com.pm.billingservice.service;

import billing.BillingResponse;
import billing.DeleteBillByIdRequest;
import com.pm.billingservice.dto.BillRequestDTO;
import com.pm.billingservice.dto.BillResponseDTO;
import com.pm.billingservice.kafka.KafkaProducer;
import com.pm.billingservice.mapper.BillMapper;
import com.pm.billingservice.model.Bill;
import com.pm.billingservice.repository.BillRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.UUID;

import static org.hibernate.internal.util.collections.ArrayHelper.forEach;

@Service
public class BillingService {
    private static final Logger log = LoggerFactory.getLogger(BillingService.class);
    private final BillRepository billRepository;
    private final KafkaProducer kafkaProducer;

    public BillingService(BillRepository billRepository, KafkaProducer kafkaProducer) {
        this.billRepository = billRepository;
        this.kafkaProducer = kafkaProducer;
    }

    public List<Bill> getBills() {
        List<Bill> bills = new ArrayList<>();
        return bills;
    }

    public Bill createBill(billing.BillingRequest billingRequest) {

        Bill bill = new Bill();
        bill.setCustomerId(UUID.fromString(billingRequest.getCustomerId()));
        bill.setIssueDate(LocalDate.now());
        bill.setDueDate(bill.getIssueDate().plusDays(30));
        bill.setAmount(Double.valueOf(billingRequest.getAmount()));
        Bill newBill = billRepository.save(bill);
        kafkaProducer.sendBillCreateEvent(newBill);
        return bill;
    }

    public List<Bill> getBills(billing.GetBillsByIdRequest billingRequest) {
        List<Bill> bills = billRepository.findByCustomerId(UUID.fromString(billingRequest.getCustomerId()));
        return bills;
    }

    public boolean deleteBill(DeleteBillByIdRequest request) {
        boolean status = false;
        Bill bill = billRepository.getReferenceById(UUID.fromString(request.getId()));
       if( billRepository.findById(UUID.fromString(request.getId())).isEmpty() ) {
           log.info("Couldn't find a bill with id: {}" , request.getId());
       } else {
           billRepository.deleteById(UUID.fromString(request.getId()));
           status = true;
           kafkaProducer.sendBillDeleteEvent(bill);
           log.info("Bill has been deleted: {}", bill.getId());
       }

       return status;
    }


}
