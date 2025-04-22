package com.pm.analyticsservice.kafka;

import com.google.protobuf.InvalidProtocolBufferException;
import com.pm.analyticsservice.model.CustomerAnalytics;
import com.pm.analyticsservice.repository.CustomerAnalyticsRepository;
import customer.events.CustomerEvent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.UUID;

@Service
public class KafkaConsumer {

    private final CustomerAnalyticsRepository customerAnalyticsRepository;

    public KafkaConsumer (CustomerAnalyticsRepository customerAnalyticsRepository) {
        this.customerAnalyticsRepository = customerAnalyticsRepository;
    }

    private static final Logger log = LoggerFactory.getLogger(KafkaConsumer.class);

    @KafkaListener(topics = "customer", groupId = "analytics-service")
    public void consumeEvent (byte[] event) {

        try{
            CustomerEvent customerEvent = CustomerEvent.parseFrom(event);
            // Check event type;
            if(customerEvent.getEventType().equals("CUSTOMER_CREATED")) {
                CustomerAnalytics customer = new CustomerAnalytics();
                customer.setId(UUID.fromString(customerEvent.getCustomerId()));
                customer.setRegisteredDate(LocalDate.parse(customerEvent.getRegisterDate()));
                customerAnalyticsRepository.save(customer);
                log.info("Customer created : {} , {}", customer.getId(), customer.getRegisteredDate());
            } else if (customerEvent.getEventType().equals("CUSTOMER_DELETED")) {
                customerAnalyticsRepository.deleteById(UUID.fromString(customerEvent.getCustomerId()));
                log.info("Customer deleted by id : {}" , customerEvent.getCustomerId() );
            }

        } catch (InvalidProtocolBufferException e) {
            log.info("Error deserializing event: {}", e.getMessage());
        }
    }
}
