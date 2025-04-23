package com.pm.analyticsservice.repository;

import com.pm.analyticsservice.model.CustomerAnalytics;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.UUID;

public interface CustomerAnalyticsRepository extends JpaRepository<CustomerAnalytics, UUID> {

    @Query("SELECT COUNT(c) FROM CustomerAnalytics c")
    int countCustomersAsInt();

}
