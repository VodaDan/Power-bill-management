package com.pm.analyticsservice.controller;

import com.pm.analyticsservice.Service.AnalyticsService;
import com.pm.analyticsservice.dto.AnalyticsResponseDTO;
import com.pm.analyticsservice.dto.CustomerAnalyticsResponseDTO;
import com.pm.analyticsservice.model.CustomerAnalytics;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/analytics")
public class AnalyticsController {

    private final AnalyticsService analyticsService;

    public AnalyticsController(AnalyticsService analyticsService) {
        this.analyticsService = analyticsService;
    }

    @GetMapping
    public ResponseEntity<AnalyticsResponseDTO> getAnalytics(){
        AnalyticsResponseDTO analyticsResponseDTO = analyticsService.generateAnalytics();
        return  ResponseEntity.ok().body(analyticsResponseDTO);
    }

}
