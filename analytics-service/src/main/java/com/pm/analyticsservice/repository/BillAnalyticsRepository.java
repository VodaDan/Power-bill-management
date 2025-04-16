package com.pm.analyticsservice.repository;

import com.pm.analyticsservice.model.BillAnalytics;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;


public interface BillAnalyticsRepository extends JpaRepository<BillAnalytics, UUID> {
}
