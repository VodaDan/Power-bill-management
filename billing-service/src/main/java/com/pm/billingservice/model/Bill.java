package com.pm.billingservice.model;

import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.validation.constraints.NotNull;

import java.time.LocalDate;
import java.util.UUID;

public class Bill {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private UUID id;

    @NotNull
    private LocalDate issueDate;

    @NotNull
    private LocalDate dueDate;

    @NotNull
    private double amount;

    @NotNull
    private UUID customerId;
}
