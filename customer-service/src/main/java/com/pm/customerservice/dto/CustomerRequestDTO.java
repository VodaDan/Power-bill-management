package com.pm.customerservice.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public class CustomerRequestDTO {

    @NotBlank (message = "Name is required!")
    @Size(max = 100, message = "Name cannot be longer than 100 characters")
    private String name;

    @NotBlank (message = "Email is required")
    @Email
    private String email;

    @NotBlank (message = "Address cannot be blank!")
    private String address;

    @NotBlank (message = "Register date is required!")
    private String registerDate;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getRegisterDate() {
        return registerDate;
    }

    public void setRegisterDate(String registerDate) {
        this.registerDate = registerDate;
    }
}
