package com.pm.customerservice.service;

import com.google.protobuf.Any;
import com.pm.customerservice.dto.CustomerRequestDTO;
import com.pm.customerservice.dto.CustomerResponseDTO;
import com.pm.customerservice.exception.AddressAlreadyExistsException;
import com.pm.customerservice.kafka.KafkaProducer;
import com.pm.customerservice.mapper.CustomerMapper;
import com.pm.customerservice.module.Customer;
import com.pm.customerservice.repository.CustomerRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.*;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.jdbc.EmbeddedDatabaseConnection;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.time.LocalDate;
import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.*;


@ExtendWith(MockitoExtension.class)
public class CustomerServiceUnitTests {

//    @Autowired
//    private CustomerRepository customerRepository;

    @Mock
    private CustomerRepository customerRepository;


    @Mock
    private KafkaProducer kafkaProducer;

    @InjectMocks
    private CustomerService customerService;


    // Update Customers Tests
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

        given(customerRepository.findById(customer.getId())).willReturn(Optional.of(customer));
        given(customerRepository.existsByAddressAndIdNot(customer.getAddress(),customer.getId())).willReturn(true);

        Exception exception = assertThrows(AddressAlreadyExistsException.class, () -> {
            customerService.updateCustomer(customer.getId(), request);
        });
        assertEquals("Address '"+ request.getAddress() + "' is already registered in our system !" , exception.getMessage());
    }


    // Get Customers Tests
    @Test
    public void CustomerServiceGetCustomerReturnsList() {
        Customer mockCustomer = createMockCustomer();
        Customer mockCustomer2 = createMockCustomer();
        when(customerRepository.findAll()).thenReturn(List.of(mockCustomer, mockCustomer2));
        List<CustomerResponseDTO> list = customerService.getCustomers();

        assertNotNull(list);
        assertEquals(2, list.size());
    }

    @Test
    public void CustomerServiceGetCustomerReturnsEmptyWhenNoCustomers() {
        when(customerRepository.findAll()).thenReturn(Collections.emptyList());
        List<CustomerResponseDTO> list = customerService.getCustomers();

        assertTrue(list.isEmpty());
        assertEquals(0, list.size());

    }

    // Create Customer Tests

    @Test
    public void CustomerServiceCreateCustomerReturnsCustomerResponseDTO() {

        Customer customerModel = createMockCustomer();
        CustomerRequestDTO customer = CustomerMapper.toRequest(customerModel);
        when(customerRepository.existsByAddress(customer.getAddress())).thenReturn(false);
        when(customerRepository.save(any(Customer.class))).thenReturn(customerModel);
        CustomerResponseDTO response = customerService.createCustomer(customer);

        assertNotNull(response);
        assertEquals(CustomerResponseDTO.class,response.getClass());
        assertEquals(response.getName(), customer.getName());
        assertEquals(response.getAddress(), customer.getAddress());
        assertEquals(response.getEmail(),customer.getEmail());

    }

    @Test
    public void CustomerServiceCreateCustomerThrowsExceptionIfAddressExists() {
        Customer customerModel = createMockCustomer();
        CustomerRequestDTO customer = CustomerMapper.toRequest(customerModel);
        when(customerRepository.existsByAddress(customer.getAddress())).thenReturn(true);

        AddressAlreadyExistsException exception = assertThrows(AddressAlreadyExistsException.class, () -> customerService.createCustomer(customer));

        assertEquals("Address '"+ customer.getAddress() + "' is already registered in our system !", exception.getMessage());
    }

    // Delete Customer Tests

    @Test
    public void CustomerServiceDeleteCustomerReturnsResponseDto() {
        Customer customer = createMockCustomer();

        customerRepository.save(customer);

        when(customerRepository.findById(any(UUID.class))).thenReturn(Optional.of(customer));

        CustomerResponseDTO responseDTO = customerService.deleteCustomer(customer.getId());

        assertNotNull(responseDTO);
        assertEquals(responseDTO.getId(), String.valueOf(customer.getId()));
        assertEquals(responseDTO.getName(), customer.getName());
        assertEquals(responseDTO.getEmail(), customer.getEmail());
        assertEquals(responseDTO.getAddress(), customer.getAddress());

    }

    @Captor
    private ArgumentCaptor<Customer> customerCaptor;

    @Test
    public void CustomerServiceCreateCustomerSendEventKafka() {
        Customer customerModel = createMockCustomer();
        CustomerRequestDTO customer = CustomerMapper.toRequest(customerModel);

        when(customerRepository.existsByAddress(customer.getAddress())).thenReturn(false);
        when(customerRepository.save(any(Customer.class))).thenReturn(customerModel);



        customerService.createCustomer(customer);
        verify(kafkaProducer).sendCreateEvent(customerCaptor.capture());
        Customer capturedCustomer = customerCaptor.getValue();

        verify(kafkaProducer, times(1)).sendCreateEvent(capturedCustomer);
        assertEquals(capturedCustomer.getAddress(), customerModel.getAddress());
    }


    // Mocks

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
