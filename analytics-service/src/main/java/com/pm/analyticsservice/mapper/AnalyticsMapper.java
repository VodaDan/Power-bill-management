package com.pm.analyticsservice.mapper;

import com.pm.analyticsservice.dto.AnalyticsResponseDTO;
import com.pm.analyticsservice.model.Analytics;

public class AnalyticsMapper {

    public static AnalyticsResponseDTO toDTO (Analytics analytics) {
        AnalyticsResponseDTO DTO = new AnalyticsResponseDTO();
        DTO.setTotalBills(String.valueOf(analytics.getTotalBills()));
        DTO.setTotalCustomers(String.valueOf(analytics.getTotalCustomers()));
        DTO.setAverageBillRevenue(String.valueOf(analytics.getAverageBillRevenue()));
        DTO.setTotalRevenue(String.valueOf(analytics.getTotalRevenue()));
        DTO.setGenerationDate(String.valueOf(analytics.getGenerationDate()));
        return DTO;
    }


}
