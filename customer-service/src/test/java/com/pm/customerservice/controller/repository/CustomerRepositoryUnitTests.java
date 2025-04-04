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
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
@AutoConfigureTestDatabase(connection = EmbeddedDatabaseConnection.H2)
@ExtendWith(MockitoExtension.class)
public class CustomerRepositoryUnitTests {
    @Autowired
    private CustomerRepository customerRepository;

    @Test
    public void CustomerRepositorySaveReturnCustomer() {
        Customer customer = createMockCustomer();

        Customer savedCustomer = customerRepository.save(customer);

        assertNotNull(savedCustomer);
        assertNotNull(savedCustomer.getId());
        assertEquals("Test name",savedCustomer.getName());
        assertEquals(customer.getAddress(),savedCustomer.getAddress());
        assertEquals("TestEmail@yahoo.com",savedCustomer.getEmail());

    }

    @Test
    public void CustomeRepositoryFindAllCustomers() {
        Customer firstCustomer = createMockCustomer();
        Customer secondCustomer = createMockCustomer();

        customerRepository.save(firstCustomer);
        customerRepository.save(secondCustomer);

        List<Customer> customerList = customerRepository.findAll();

        assertNotNull(customerList);
        assertEquals(2,customerList.size());


    }

    @Test
    public void CustomerRepositoryFindById() {
        Customer firstCustomer = createMockCustomer();
        Customer secondCustomer = createMockCustomer();

        customerRepository.save(firstCustomer);
        customerRepository.save(secondCustomer);

        Optional<Customer> findFirstCustomer = customerRepository.findById(firstCustomer.getId());

        assertNotNull(findFirstCustomer);
        assertEquals(firstCustomer.getId(),findFirstCustomer.get().getId());
    }

    @Test
    public void CustomerRepositoryFindByIdNonExistentCustomer() {

        // Passing a random UUID
        Optional<Customer> findNonExistentCustomer = customerRepository.findById(UUID.randomUUID());

        assertTrue(findNonExistentCustomer.isEmpty());
    }

    private static int addressCounter = 0 ;

    // Method to create a mock Customer object
    private Customer createMockCustomer() {
        Customer customer = new Customer();
        customer.setName("Test name");
        customer.setAddress("Test address" + addressCounter++ );
        customer.setEmail("TestEmail@yahoo.com");
        customer.setRegisterDate(LocalDate.now());
        return customer;
    }
}
