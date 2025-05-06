package com.pm.customerservice.controller;

import com.pm.customerservice.dto.CustomerRequestDTO;
import com.pm.customerservice.dto.CustomerResponseDTO;
import com.pm.customerservice.mapper.CustomerMapper;
import com.pm.customerservice.module.Customer;
import com.pm.customerservice.service.CustomerService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;


import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/customers")
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

    @GetMapping("/{email}")
    public ResponseEntity<CustomerResponseDTO> getCustomerInfo(@PathVariable String email) {
        CustomerResponseDTO customerResponseDTO = customerService.getCustomerByEmail(email);
        return ResponseEntity.ok().body(customerResponseDTO);
    }

    @PostMapping
    public ResponseEntity<CustomerResponseDTO> createCustomer(@RequestBody CustomerRequestDTO customerRequestDTO) {
        CustomerResponseDTO customerResponseDTO = customerService.createCustomer(customerRequestDTO);
        return ResponseEntity.ok().body(customerResponseDTO);
    }

    @PutMapping("/{id}")
    public ResponseEntity<CustomerResponseDTO> updateCustomer(@PathVariable UUID id , @RequestBody CustomerRequestDTO customerRequestDTO) {
        CustomerResponseDTO customerResponseDTO = customerService.updateCustomer(id,customerRequestDTO);
        return ResponseEntity.ok().body(customerResponseDTO);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<CustomerResponseDTO> deleteCustomer(@PathVariable UUID id) {
        CustomerResponseDTO customerResponseDTO = customerService.deleteCustomer(id);

        return ResponseEntity.ok().body(customerResponseDTO);
    }
}
