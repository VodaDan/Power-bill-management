package com.pm.billingservice.controller;

import com.pm.billingservice.dto.BillResponseDTO;
import com.pm.billingservice.service.BillingService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import java.util.List;

@RestController
@RequestMapping("/bills")
public class BillController {

    private static final Logger log = LoggerFactory.getLogger(BillController.class);
    private final BillingService billingService;

    public BillController (BillingService billingService) {
        this.billingService = billingService;
    }

    @GetMapping("/{id}")
    public ResponseEntity<List<BillResponseDTO>> getBillsById(@PathVariable String id) {
        List<BillResponseDTO> list = billingService.getBillsById(id);
        log.info("getBillsById() called");
        return ResponseEntity.ok().body(list);
    }
}
