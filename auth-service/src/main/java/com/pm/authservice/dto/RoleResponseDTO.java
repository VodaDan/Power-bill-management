package com.pm.authservice.dto;

public class RoleResponseDTO {

    private String role;


    // This could be a Setter but when you have a small class with only 1 parameter this could be used;
    public RoleResponseDTO (String role) {
        this.role = role;
    }

    public String getRole() {
        return this.role;
    }
}
