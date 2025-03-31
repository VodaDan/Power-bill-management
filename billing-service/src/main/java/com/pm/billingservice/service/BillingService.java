package com.pm.billingservice.service;

import com.pm.billingservice.model.Bill;
import com.pm.billingservice.repository.BillRepository;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

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

}
