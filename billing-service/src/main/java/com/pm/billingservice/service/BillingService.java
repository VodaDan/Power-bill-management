package com.pm.billingservice.service;

import billing.BillingResponse;
import com.pm.billingservice.dto.BillRequestDTO;
import com.pm.billingservice.dto.BillResponseDTO;
import com.pm.billingservice.mapper.BillMapper;
import com.pm.billingservice.model.Bill;
import com.pm.billingservice.repository.BillRepository;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.UUID;

import static org.hibernate.internal.util.collections.ArrayHelper.forEach;

@Service
public class BillingService {
    private final BillRepository billRepository;

    public BillingService(BillRepository billRepository) {
        this.billRepository = billRepository;
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
        billRepository.save(bill);

        return bill;
    }

    public List<Bill> getBills(billing.BillingRequest billingRequest) {
        List<Bill> bills = billRepository.findByCustomerId(UUID.fromString(billingRequest.getCustomerId()));
        return bills;
    }
}
