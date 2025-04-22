package com.pm.analyticsservice.model;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.validation.constraints.NotNull;
import org.springframework.cglib.core.Local;

import java.time.LocalDate;
import java.util.UUID;

@Entity
public class Analytics {


    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private UUID id;

    @NotNull
    private int totalCustomers;

    @NotNull
    private int totalBills;

    @NotNull
    private double totalRevenue;

    @NotNull
    private double averageBillRevenue;

    @NotNull
    private LocalDate generationDate;


    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public int getTotalCustomers() {
        return totalCustomers;
    }

    public void setTotalCustomers(int totalCustomers) {
        this.totalCustomers = totalCustomers;
    }

    public int getTotalBills() {
        return totalBills;
    }

    public void setTotalBills(int totalBills) {
        this.totalBills = totalBills;
    }

    public double getTotalRevenue() {
        return totalRevenue;
    }

    public void setTotalRevenue(double totalRevenue) {
        this.totalRevenue = totalRevenue;
    }

    public double getAverageBillRevenue() {
        return averageBillRevenue;
    }

    public void setAverageBillRevenue(double averageBillRevenue) {
        this.averageBillRevenue = averageBillRevenue;
    }

    public LocalDate getGenerationDate() {
        return generationDate;
    }

    public void setGenerationDate(LocalDate generationDate) {
        this.generationDate = generationDate;
    }
}
