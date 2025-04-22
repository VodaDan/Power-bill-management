package com.pm.analyticsservice.dto;

public class AnalyticsResponseDTO {

    private String totalCustomers;
    private String totalBills;
    private String totalRevenue;
    private String averageBillRevenue;



    private String generationDate;

    public String getTotalCustomers() {
        return totalCustomers;
    }

    public void setTotalCustomers(String totalCustomers) {
        this.totalCustomers = totalCustomers;
    }

    public String getTotalBills() {
        return totalBills;
    }

    public void setTotalBills(String totalBills) {
        this.totalBills = totalBills;
    }

    public String getTotalRevenue() {
        return totalRevenue;
    }

    public void setTotalRevenue(String totalRevenue) {
        this.totalRevenue = totalRevenue;
    }

    public String getAverageBillRevenue() {
        return averageBillRevenue;
    }

    public void setAverageBillRevenue(String averageBillRevenue) {
        this.averageBillRevenue = averageBillRevenue;
    }

    public String getGenerationDate() {
        return generationDate;
    }

    public void setGenerationDate(String generationDate) {
        this.generationDate = generationDate;
    }
}
