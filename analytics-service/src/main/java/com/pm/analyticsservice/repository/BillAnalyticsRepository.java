package com.pm.analyticsservice.repository;

import com.pm.analyticsservice.model.BillAnalytics;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.UUID;


public interface BillAnalyticsRepository extends JpaRepository<BillAnalytics, UUID> {

    @Query("SELECT COUNT(b) FROM BillAnalytics b")
    int countBillsAsInt();

    @Query("SELECT SUM(b.amount) FROM BillAnalytics b")
    double sumAllBills();
}
