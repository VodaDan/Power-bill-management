package com.pm.customerservice.mapper;

import com.pm.customerservice.dto.CustomerRequestDTO;
import com.pm.customerservice.dto.CustomerResponseDTO;
import com.pm.customerservice.module.Customer;

import java.time.LocalDate;

public class CustomerMapper {

    public static CustomerResponseDTO toDTO(Customer customer){
        CustomerResponseDTO customerResponseDTO = new CustomerResponseDTO();
        customerResponseDTO.setId(customer.getId().toString());
        customerResponseDTO.setName(customer.getName());
        customerResponseDTO.setEmail(customer.getEmail());
        customerResponseDTO.setAddress(customer.getAddress());

        return customerResponseDTO;
    }

    public static Customer toModel(CustomerRequestDTO customerRequestDTO) {
        Customer customer = new Customer();
        customer.setName(customerRequestDTO.getName());
        customer.setEmail(customerRequestDTO.getEmail());
        customer.setAddress(customerRequestDTO.getAddress());
        customer.setRegisterDate(LocalDate.parse(customerRequestDTO.getRegisterDate()));
        return customer;
    }
}
