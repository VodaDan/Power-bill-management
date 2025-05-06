package com.pm.billingservice.controller;

import com.pm.billingservice.dto.BillResponseDTO;
import com.pm.billingservice.service.BillingService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import java.util.List;

@RestController
@RequestMapping("/bills")
public class BillController {

    private final BillingService billingService;

    public BillController (BillingService billingService) {
        this.billingService = billingService;
    }

    @GetMapping("/bills/${id}")
    public ResponseEntity<List<BillResponseDTO>> getBillsById(@PathVariable String id) {
        List<BillResponseDTO> list = billingService.getBillsById(id);
        return ResponseEntity.ok().body(list);
    }
}
