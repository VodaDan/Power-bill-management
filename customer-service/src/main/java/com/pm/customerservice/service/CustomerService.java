package com.pm.customerservice.service;

import com.pm.customerservice.dto.CustomerResponseDTO;
import com.pm.customerservice.dto.CustomerRequestDTO;
import com.pm.customerservice.exception.AddressAlreadyExistsException;
import com.pm.customerservice.exception.CustomerNotFoundException;
import com.pm.customerservice.mapper.CustomerMapper;
import com.pm.customerservice.module.Customer;
import com.pm.customerservice.repository.CustomerRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

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
        if(customerRepository.existsByAddress(customerRequestDTO.getAddress())){
            throw new AddressAlreadyExistsException("Address '"+ customerRequestDTO.getAddress() + "' is already registered in our system !");
        }

        Customer customer = customerRepository.save(CustomerMapper.toModel(customerRequestDTO));
        return CustomerMapper.toDTO(customer);
    }

    public CustomerResponseDTO updateCustomer(UUID id , CustomerRequestDTO customerRequestDTO) {
        Customer customer = customerRepository.findById(id).orElseThrow(() -> new CustomerNotFoundException("Customer not found!"));
        if(customerRepository.existsByAddressAndIdNot(customerRequestDTO.getAddress(), id)){
            throw new AddressAlreadyExistsException("Address '"+ customerRequestDTO.getAddress() + "' is already registered in our system !");
        }
        customer.setName(customerRequestDTO.getName());
        customer.setEmail(customerRequestDTO.getEmail());
        customer.setAddress(customerRequestDTO.getAddress());

        Customer updatedCustomer = customerRepository.save(customer);

        return CustomerMapper.toDTO(updatedCustomer);
    }
}
