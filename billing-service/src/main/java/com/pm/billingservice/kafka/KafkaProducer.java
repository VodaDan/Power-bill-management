package com.pm.billingservice.kafka;

import billing.events.BillingEvent;
import com.pm.billingservice.model.Bill;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

@Service
public class KafkaProducer {

    private static final Logger log = LoggerFactory.getLogger(KafkaProducer.class);
    private final KafkaTemplate<String, byte[]> kafkaTemplate;

    public KafkaProducer (KafkaTemplate<String, byte[]> kafkaTemplate) {
        this.kafkaTemplate = kafkaTemplate;
    }

    public void sendBillCreateEvent(Bill bill) {
        BillingEvent event = BillingEvent.newBuilder()
                .setEventType("BILL_CREATE")
                .setId(bill.getId().toString())
                .setCustomerId(bill.getId().toString())
                .setAmount(String.valueOf(bill.getAmount()))
                .setDueDate(bill.getDueDate().toString())
                .setIssueDate(bill.getIssueDate().toString())
                .build();
        try {
            kafkaTemplate.send("bill", event.toByteArray());
        } catch (Exception e) {
            log.error("Error sending BILL_CREATE event: {}", e.getMessage());
        }
    }

    public void sendBillDeleteEvent (Bill bill) {
        BillingEvent event = BillingEvent.newBuilder()
                .setEventType("BILL_DELETE")
                .setId(bill.getId().toString())
                .build();
        try{
            kafkaTemplate.send("bill", event.toByteArray());
        } catch (Exception e) {
            log.error("Error sending BILL_DELETE event: {}", e.getMessage());
        }
    }
}
