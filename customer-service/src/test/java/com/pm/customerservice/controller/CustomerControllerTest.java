//package com.pm.customerservice.controller;
//
//import com.pm.customerservice.dto.CustomerResponseDTO;
//import com.pm.customerservice.exception.AddressAlreadyExistsException;
//import com.pm.customerservice.exception.CustomerNotFoundException;
//import com.pm.customerservice.service.CustomerService;
//import org.junit.jupiter.api.Test;
//import org.mockito.InjectMocks;
//import org.mockito.Mock;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
//
//import org.springframework.http.MediaType;
//import org.springframework.test.web.servlet.MockMvc;
//
//
//import java.util.UUID;
//
//import static org.mockito.ArgumentMatchers.any;
//import static org.mockito.ArgumentMatchers.eq;
//import static org.mockito.Mockito.when;
//import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
//import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
//import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
//import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
//
//@WebMvcTest (CustomerController.class)
//public class CustomerControllerTest {
//
//    @Autowired
//    private MockMvc mockMvc;
//
//    @Mock
//    private CustomerService customerService;
//
//    @InjectMocks
//    private CustomerController customerController;
//
//    @Test
//    void testUpdateCustomer_AddressAlreadyExists() throws Exception {
//        UUID existingCustomerId = UUID.randomUUID();
//
//        when(customerService.updateCustomer(eq(existingCustomerId), any(com.pm.customerservice.dto.CustomerRequestDTO.class)))
//                .thenThrow(new AddressAlreadyExistsException("Address already exists"));
//
//        mockMvc.perform(put("/customers/" + existingCustomerId)
//                        .contentType(MediaType.APPLICATION_JSON)
//                        .content("{\"name\": \"Jane Doe\", \"email\": \"jane.doe@example.com\", \"address\": \"Existing Address\"}"))
//                .andExpect(status().isBadRequest()) // Expect 400 Bad Request
//                .andExpect(jsonPath("$.message").value("Address already exists"));
//    }
//}
