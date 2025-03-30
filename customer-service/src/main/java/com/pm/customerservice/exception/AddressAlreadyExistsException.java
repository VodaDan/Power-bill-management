package com.pm.customerservice.exception;

public class AddressAlreadyExistsException extends RuntimeException {
  public AddressAlreadyExistsException(String message) {
    super(message);
  }
}
