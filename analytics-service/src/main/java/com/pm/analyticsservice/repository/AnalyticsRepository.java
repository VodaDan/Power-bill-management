package com.pm.analyticsservice.repository;

import com.pm.analyticsservice.model.Analytics;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface AnalyticsRepository  extends JpaRepository<Analytics, UUID> {

}
