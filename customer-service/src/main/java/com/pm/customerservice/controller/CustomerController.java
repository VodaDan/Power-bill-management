package com.pm.customerservice.controller;

import com.pm.customerservice.dto.CustomerResponseDTO;
import com.pm.customerservice.service.CustomerService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;

@RestController("/customers")
public class CustomerController {

    private final CustomerService customerService;

    public CustomerController (CustomerService customerService) {
        this.customerService = customerService;
    }

    @GetMapping
    public ResponseEntity<List<CustomerResponseDTO>> getCustomers() {
        List<CustomerResponseDTO> customerResponseDTO = customerService.getCustomers();
        return ResponseEntity.ok().body(customerResponseDTO);
    }
}
