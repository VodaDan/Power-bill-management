package com.pm.customerservice.mapper;

import com.pm.customerservice.dto.CustomerRequestDTO;
import com.pm.customerservice.dto.CustomerResponseDTO;
import com.pm.customerservice.module.Customer;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import static org.junit.jupiter.api.Assertions.*;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertNull;

@ExtendWith(MockitoExtension.class)
public class CustomerMapperUnitTests {

    @Test
    public void CustomerMapperToDTOReturnsDTO () {

        Customer newCustomer = new Customer();
        newCustomer.setId(UUID.randomUUID());
        newCustomer.setEmail("test@email.com");
        newCustomer.setName("test");
        newCustomer.setAddress("test");

        CustomerResponseDTO responseDTO = CustomerMapper.toDTO(newCustomer);

        assertNotNull(responseDTO);
        assertEquals(responseDTO.getName(), newCustomer.getName());
        assertEquals(responseDTO.getEmail(), newCustomer.getEmail());
        assertEquals(responseDTO.getAddress(), newCustomer.getAddress());
        assertEquals(responseDTO.getId(), newCustomer.getId().toString());

    }

    @Test
    public void CustomerMapperToModelReturnsCustomer () {
        CustomerRequestDTO requestDTO = new CustomerRequestDTO();
        requestDTO.setName("test");
        requestDTO.setEmail("test@test.com");
        requestDTO.setAddress("test_address");
        requestDTO.setRegisterDate("2000-02-02");

        Customer customer = CustomerMapper.toModel(requestDTO);

        assertNotNull(customer);
        assertEquals(customer.getName(), requestDTO.getName());
        assertEquals(customer.getEmail(), requestDTO.getEmail());
        assertEquals(customer.getAddress(), requestDTO.getAddress());
        assertEquals(customer.getRegisterDate().toString(), requestDTO.getRegisterDate());
    }

    @Test
    public void CustomerMapperToRequestReturnsRequestDTO () {
        Customer newCustomer = new Customer();
        newCustomer.setId(UUID.randomUUID());
        newCustomer.setEmail("test@email.com");
        newCustomer.setName("test");
        newCustomer.setAddress("test");

        CustomerRequestDTO request = CustomerMapper.toRequest(newCustomer);

        assertNotNull(request);
        assertEquals(request.getName(), newCustomer.getName());
        assertEquals(request.getEmail(), newCustomer.getEmail());
        assertEquals(request.getAddress(), newCustomer.getAddress());
        assertEquals(request.getRegisterDate(), String.valueOf(newCustomer.getRegisterDate()));
    }

}
