package com.pm.analyticsservice.repository;

import com.pm.analyticsservice.model.CustomerAnalytics;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface CustomerAnalyticsRepository extends JpaRepository<CustomerAnalytics, UUID> {
}
