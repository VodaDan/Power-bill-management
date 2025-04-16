package com.pm.analyticsservice.model;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.validation.constraints.NotNull;

import java.time.LocalDate;
import java.util.UUID;

@Entity
public class CustomerAnalytics {

    @Id
    private UUID id;

    @NotNull
    private LocalDate registerDate;

}
