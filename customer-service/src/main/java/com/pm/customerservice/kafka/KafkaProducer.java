package com.pm.customerservice.kafka;

import com.pm.customerservice.module.Customer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;
import customer.events.*;

@Service
public class KafkaProducer {

    private static final Logger log = LoggerFactory.getLogger(KafkaProducer.class);
    private final KafkaTemplate<String, byte[]> kafkaTemplate;

    public KafkaProducer(KafkaTemplate<String, byte[]> kafkaTemplate) {
        this.kafkaTemplate = kafkaTemplate;
    }

    public void sendCreateEvent(Customer customer) {
        CustomerEvent event = CustomerEvent.newBuilder()
                .setCustomerId(customer.getId().toString())
                .setRegisterDate(customer.getRegisterDate().toString())
                .setEventType("CUSTOMER_CREATED")
                .build();
        try {
            kafkaTemplate.send("customer", event.toByteArray());
        } catch (Exception e) {
            log.error("Error sending CustomerCreated event: {}" , e.getMessage());
        }
    }

    public void sendDeleteEvent(Customer customer) {
        CustomerEvent event = CustomerEvent.newBuilder()
                .setCustomerId(customer.getId().toString())
                .setRegisterDate(customer.getRegisterDate().toString())
                .setEventType("CUSTOMER_DELETED")
                .build();
        try {
            kafkaTemplate.send("customer", event.toByteArray());
        } catch (Exception e) {
            log.error("Error sending CustomerDeleted event: {}" , e.getMessage());
        }
    }


}
