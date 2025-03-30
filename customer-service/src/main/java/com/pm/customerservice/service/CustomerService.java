package com.pm.customerservice.service;

import com.pm.customerservice.dto.CustomerResponseDTO;
import com.pm.customerservice.dto.CustomerRequestDTO;
import com.pm.customerservice.mapper.CustomerMapper;
import com.pm.customerservice.module.Customer;
import com.pm.customerservice.repository.CustomerRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CustomerService {

    private final CustomerRepository customerRepository;

    public CustomerService(CustomerRepository customerRepository) {
        this.customerRepository = customerRepository;
    }

    public List<CustomerResponseDTO> getCustomers() {
        List<Customer> customers = customerRepository.findAll();
        List<CustomerResponseDTO> customerResponseDTO = customers.stream().map(CustomerMapper::toDTO).toList();
        return customerResponseDTO;
    }

    public CustomerResponseDTO createCustomer(CustomerRequestDTO customerRequestDTO) {
        Customer customer = customerRepository.save(CustomerMapper.toModel(customerRequestDTO));
        return CustomerMapper.toDTO(customer);
    }
}
