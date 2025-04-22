package com.pm.analyticsservice.kafka;

import billing.events.BillingEvent;
import com.google.protobuf.InvalidProtocolBufferException;
import com.pm.analyticsservice.model.BillAnalytics;
import com.pm.analyticsservice.model.CustomerAnalytics;
import com.pm.analyticsservice.repository.BillAnalyticsRepository;
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
    private final BillAnalyticsRepository billAnalyticsRepository;

    public KafkaConsumer (CustomerAnalyticsRepository customerAnalyticsRepository, BillAnalyticsRepository billAnalyticsRepository) {
        this.customerAnalyticsRepository = customerAnalyticsRepository;
        this.billAnalyticsRepository = billAnalyticsRepository;
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
            log.info("Error deserializing customer event: {}", e.getMessage());
        }
    }

    @KafkaListener(topics = "bill", groupId = "analytics-service")
    public void consumeBillingEvent (byte[] event) {
        try {
            BillingEvent billingEvent = BillingEvent.parseFrom(event);

            if(billingEvent.getEventType().equals("BILL_CREATE")) {
                BillAnalytics bill = new BillAnalytics();
                bill.setId(UUID.fromString(billingEvent.getId()));
                bill.setCustomerId(UUID.fromString(billingEvent.getCustomerId()));
                bill.setAmount(Double.valueOf(billingEvent.getAmount()));
                bill.setDueDate(LocalDate.parse(billingEvent.getDueDate()));
                bill.setIssueDate(LocalDate.parse(billingEvent.getIssueDate()));
                billAnalyticsRepository.save(bill);
                log.info("Bill created: {} , {}" , billingEvent.getId(), billingEvent.getAmount());

            } else if(billingEvent.getEventType().equals("BILL_DELETE")) {
                billAnalyticsRepository.deleteById(UUID.fromString(billingEvent.getId()));
                log.info("Bill deleted with id: {}" , billingEvent.getId());
            }

        } catch (InvalidProtocolBufferException e) {
            log.info("Error deserializing billing event: {}", e.getMessage());
        }
    }
}
