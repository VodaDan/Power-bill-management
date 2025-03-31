package com.pm.billingservice.service;

import com.pm.billingservice.model.Bill;
import com.pm.billingservice.repository.BillRepository;

import java.util.ArrayList;
import java.util.List;

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
