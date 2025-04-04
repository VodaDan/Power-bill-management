package com.pm.customerservice.controller.repository;

import com.pm.customerservice.module.Customer;
import com.pm.customerservice.repository.CustomerRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.jdbc.EmbeddedDatabaseConnection;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.time.LocalDate;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

@DataJpaTest
@AutoConfigureTestDatabase(connection = EmbeddedDatabaseConnection.H2)
@ExtendWith(MockitoExtension.class)
public class CustomerRepositoryUnitTests {
    @Autowired
    private CustomerRepository customerRepository;

    @Test
    public v() {
        Customer customer = createMockCustomer();

        Customer savedCustomer = customerRepository.save(customer);

        assertNotNull(savedCustomer);
        assertNotNull(savedCustomer.getId());
        assertEquals("Test name",savedCustomer.getName());
        assertEquals("Test address",savedCustomer.getAddress());
        assertEquals("Test email",savedCustomer.getEmail());

    }

    // Method to create a mock Customer object
    private Customer createMockCustomer() {
        Customer customer = new Customer();
        customer.setName("Test name");
        customer.setAddress("Test address");
        customer.setEmail("Test email");
        customer.setRegisterDate(LocalDate.now());
        return customer;
    }
}
