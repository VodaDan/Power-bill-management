package com.pm.billingservice.dto;

import jakarta.validation.constraints.NotBlank;

import java.util.UUID;

public class BillRequestDTO {
    @NotBlank(message = "Id is required!")
    private String id;

    @NotBlank(message = "Customer id required!")
    private String customerId;

    @NotBlank(message = "Amount is required!")
    private String amount;

    @NotBlank(message = "Issue Date is required!")
    private String issueDate;

    @NotBlank(message = "Due Date is required!")
    private String dueDate;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getCustomerId() {
        return customerId;
    }

    public void setCustomerId(String customerId) {
        this.customerId = customerId;
    }

    public String getAmount() {
        return amount;
    }

    public void setAmount(String amount) {
        this.amount = amount;
    }

    public String getIssueDate() {
        return issueDate;
    }

    public void setIssueDate(String issueDate) {
        this.issueDate = issueDate;
    }

    public String getDueDate() {
        return dueDate;
    }

    public void setDueDate(String dueDate) {
        this.dueDate = dueDate;
    }
}
