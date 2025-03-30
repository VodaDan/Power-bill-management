package com.pm.customerservice.mapper;

import com.pm.customerservice.dto.CustomerResponseDTO;
import com.pm.customerservice.module.Customer;

public class CustomerMapper {

    public static CustomerResponseDTO toDTO(Customer customer){
        CustomerResponseDTO customerResponseDTO = new CustomerResponseDTO();
        customerResponseDTO.setId(customer.getId().toString());
        customerResponseDTO.setName(customer.getName());
        customerResponseDTO.setEmail(customer.getEmail());
        customerResponseDTO.setAddress(customer.getAddress());

        return customerResponseDTO;
    }
}
