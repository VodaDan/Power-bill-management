package com.pm.authservice.dto;

public class LoginResponseDTO {
    private String token;


    // This could be a Setter but when you have a small class with only 1 parameter this could be used;
    public LoginResponseDTO (String token) {
        this.token = token;
    }

    public String getToken() {
        return token;
    }
}
