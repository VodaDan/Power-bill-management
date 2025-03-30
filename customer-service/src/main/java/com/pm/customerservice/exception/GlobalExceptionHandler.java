package com.pm.customerservice.exception;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import java.util.HashMap;
import java.util.Map;

@ControllerAdvice
public class GlobalExceptionHandler {

    private static final Logger log = LoggerFactory.getLogger(GlobalExceptionHandler.class);

    @ExceptionHandler (CustomerNotFoundException.class)
    public ResponseEntity<Map<String,String>> handleCustomerNotFoundException(CustomerNotFoundException ex) {
        log.warn(ex.getMessage());
        Map<String,String> errors = new HashMap<>();
        errors.put("Message",ex.getMessage());
        return ResponseEntity.badRequest().body(errors);
    }

    @ExceptionHandler (MethodArgumentNotValidException.class)
    public ResponseEntity<Map<String,String>> handleMethodArgumentNotValidException(MethodArgumentNotValidException ex) {
        log.warn(ex.getMessage());
        Map<String,String> errors = new HashMap<>();
        ex.getBindingResult().getFieldErrors().forEach(error -> errors.put(error.getField(), error.getDefaultMessage()));
        return ResponseEntity.badRequest().body(errors);
    }

    @ExceptionHandler (AddressAlreadyExistsException.class)
    public ResponseEntity<Map<String,String>> handleAddressAlreadyExistsException(AddressAlreadyExistsException ex) {
        log.warn(ex.getMessage());
        Map<String,String> errors = new HashMap<>();
        errors.put("Message",ex.getMessage());
        return ResponseEntity.badRequest().body(errors);
    }
}
