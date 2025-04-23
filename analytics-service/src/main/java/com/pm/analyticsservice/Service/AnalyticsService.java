package com.pm.analyticsservice.Service;

import com.pm.analyticsservice.dto.AnalyticsResponseDTO;
import com.pm.analyticsservice.mapper.AnalyticsMapper;
import com.pm.analyticsservice.model.Analytics;
import com.pm.analyticsservice.repository.AnalyticsRepository;
import com.pm.analyticsservice.repository.BillAnalyticsRepository;
import com.pm.analyticsservice.repository.CustomerAnalyticsRepository;
import org.springframework.cglib.core.Local;
import org.springframework.stereotype.Service;

import java.time.LocalDate;

@Service
public class AnalyticsService {

    private final CustomerAnalyticsRepository customerAnalyticsRepository;
    private final BillAnalyticsRepository billAnalyticsRepository;
    private final AnalyticsRepository analyticsRepository;

    public AnalyticsService(CustomerAnalyticsRepository customerAnalyticsRepository,
                            BillAnalyticsRepository billAnalyticsRepository,
                            AnalyticsRepository analyticsRepository) {

        this.customerAnalyticsRepository = customerAnalyticsRepository;
        this.billAnalyticsRepository = billAnalyticsRepository;
        this.analyticsRepository = analyticsRepository;
    }


    public AnalyticsResponseDTO generateAnalytics() {
        Analytics analytics = new Analytics();

        // Total Customers
        int totalCustomers = customerAnalyticsRepository.countCustomersAsInt();

        // Total Bills
        int totalBills = billAnalyticsRepository.countBillsAsInt();

        // Total Revenue
        double totalRevenue = billAnalyticsRepository.sumAllBills();

        // Average Bill Cost
        double averageBillRevenue = totalRevenue/totalBills;

        // Generation Date
        LocalDate generationDate = LocalDate.now();


        analytics.setGenerationDate(generationDate);
        analytics.setTotalCustomers(totalCustomers);
        analytics.setTotalBills(totalBills);
        analytics.setTotalRevenue(totalRevenue);
        analytics.setAverageBillRevenue(averageBillRevenue);

        analyticsRepository.save(analytics);

        return AnalyticsMapper.toDTO(analytics);
    }


}
