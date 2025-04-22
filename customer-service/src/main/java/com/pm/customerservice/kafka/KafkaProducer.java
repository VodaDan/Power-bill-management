package com.pm.customerservice.kafka;

import com.pm.customerservice.module.Customer;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;
import customer.events.*;

@Service
public class KafkaProducer {

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
    }

    public void sendDeleteEvent(Customer customer) {
        CustomerEvent event = CustomerEvent.newBuilder()
                .setCustomerId(customer.getId().toString())
                .setRegisterDate(customer.getRegisterDate().toString())
                .setEventType("CUSTOMER_DELETED")
                .build();
    }
}
