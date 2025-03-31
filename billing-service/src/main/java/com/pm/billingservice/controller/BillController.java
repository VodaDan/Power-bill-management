package com.pm.billingservice.controller;

import com.pm.billingservice.service.BillingService;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/bills")
public class BillController {

    private final BillingService billingService;

    public BillController (BillingService billingService) {
        this.billingService = billingService;
    }
}
