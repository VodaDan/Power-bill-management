package com.pm.customerservice;

import com.pm.customerservice.dto.CustomerResponseDTO;
import com.pm.customerservice.module.Customer;
import com.pm.customerservice.repository.CustomerRepository;
import com.pm.customerservice.service.CustomerService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;
import java.util.Arrays;
import java.util.List;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.BDDMockito.given;

@ExtendWith(MockitoExtension.class)
public class GetCustomersUnitTests {

    @Mock
    private CustomerRepository customerRepository;

    @InjectMocks
    private CustomerService customerService;

    @Test
    public void getCustomersHappyFlow() {

        //Arrange
        Customer customer = createMockCustomer();
        CustomerResponseDTO customerResponseDTO = createMockCustomerResponseDTO();

        given(customerRepository.findAll()).willReturn(Arrays.asList(customer));

        //Act
        List<CustomerResponseDTO> response = customerService.getCustomers();

        //Assert
        assertNotNull(response, "Response should not be null");
        assertFalse(response.isEmpty(), "Response should not be empty");

        CustomerResponseDTO newCustomerResponseDTO = response.get(0);
        assertEquals(customerResponseDTO.getName(), newCustomerResponseDTO.getName(), "Name should match");
        assertEquals(customerResponseDTO.getAddress(), newCustomerResponseDTO.getAddress(), "Address should match");
        assertEquals(customerResponseDTO.getEmail(), newCustomerResponseDTO.getEmail(), "Email should match");
    }

    // Method to create a mock Customer object
    private Customer createMockCustomer() {
        Customer customer = new Customer();
        customer.setId(UUID.fromString("123e4567-e89b-12d3-a456-426614174000"));
        customer.setName("Test name");
        customer.setAddress("Test address");
        customer.setEmail("Test email");
        customer.setRegisterDate(LocalDate.now());
        return customer;
    }

    // Method to create a mock CustomerResponseDTO object
    private CustomerResponseDTO createMockCustomerResponseDTO() {
        CustomerResponseDTO dto = new CustomerResponseDTO();
        dto.setName("Test name");
        dto.setAddress("Test address");
        dto.setEmail("Test email");
        return dto;
    }
}
