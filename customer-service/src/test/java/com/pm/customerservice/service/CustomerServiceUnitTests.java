package com.pm.customerservice.service;

import com.pm.customerservice.dto.CustomerRequestDTO;
import com.pm.customerservice.dto.CustomerResponseDTO;
import com.pm.customerservice.exception.AddressAlreadyExistsException;
import com.pm.customerservice.mapper.CustomerMapper;
import com.pm.customerservice.module.Customer;
import com.pm.customerservice.repository.CustomerRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentMatchers;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.jdbc.EmbeddedDatabaseConnection;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.time.LocalDate;
import java.util.Arrays;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.BDDMockito.given;


@ExtendWith(MockitoExtension.class)
public class CustomerServiceUnitTests {

//    @Autowired
//    private CustomerRepository customerRepository;

    @Mock
    private CustomerRepository customerRepository;

    @InjectMocks
    private CustomerService customerService;

    @Test
    public void CustomerServiceUpdateCustomerReturnsUpdated () {
        Customer customer = createMockCustomer();
        CustomerRequestDTO request = CustomerMapper.toRequest(customer);

        assertNotNull(request);

        given(customerRepository.save(ArgumentMatchers.any(Customer.class)))
                .willAnswer(invocation -> invocation.getArgument(0));
        given(customerRepository.findById(customer.getId())).willReturn(Optional.of(customer));
        given(customerRepository.existsByAddressAndIdNot(customer.getAddress(),customer.getId())).willReturn(false);

        CustomerResponseDTO checkResponseDTO = customerService.updateCustomer(customer.getId(), request);

        assertNotNull(checkResponseDTO);

        customer.setEmail("updated@email.com");
        request = CustomerMapper.toRequest(customer);

        checkResponseDTO = customerService.updateCustomer(customer.getId(),request);

        assertNotNull(checkResponseDTO);
        assertEquals("updated@email.com", checkResponseDTO.getEmail());
    }

    @Test
    public void CustomerServiceUpdateCustomerThrowsException () {
        Customer customer = createMockCustomer();
        CustomerRequestDTO request = CustomerMapper.toRequest(customer);

        assertNotNull(request);

//        given(customerRepository.save(ArgumentMatchers.any(Customer.class)))
//                .willAnswer(invocation -> invocation.getArgument(0));
        given(customerRepository.findById(customer.getId())).willReturn(Optional.of(customer));
        given(customerRepository.existsByAddressAndIdNot(customer.getAddress(),customer.getId())).willReturn(true);

        Exception exception = assertThrows(AddressAlreadyExistsException.class, () -> {
            customerService.updateCustomer(customer.getId(), request);
        });
        assertEquals("Address '"+ request.getAddress() + "' is already registered in our system !" , exception.getMessage());
    }

    private static int addressCounter = 0;

    public CustomerRequestDTO createMockRequest() {
        CustomerRequestDTO request = new CustomerRequestDTO();
        request.setName("Test");
        request.setAddress("Address " + addressCounter++ );
        request.setEmail("test@email.com");
        request.setRegisterDate(LocalDate.now().toString());
        return request;
    }

    private Customer createMockCustomer() {
        Customer customer = new Customer();
        customer.setId(UUID.randomUUID());
        customer.setName("Test Name");
        customer.setAddress("Address " + String.valueOf(addressCounter++) );
        customer.setEmail("test@email.com");
        customer.setRegisterDate(LocalDate.now());
        return customer;
    }
}
