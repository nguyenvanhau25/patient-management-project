package com.pm.billingservice.infrastructure.kafka;

import billing.events.BillingEvent;
import com.pm.billingservice.application.dto.BillingEventDto;
import com.pm.billingservice.domain.BillingTransaction;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class KafkaProducer {
    private static final Logger log = LoggerFactory.getLogger( KafkaProducer.class);
    private final KafkaTemplate<String, byte[]> kafkaTemplate;

    public void sendEvent(BillingEventDto tx) {
        BillingEvent event = BillingEvent.newBuilder()
                .setEventType("BILLING_TRANSACTION_COMPLETED")
                .setTransactionId(tx.getTransactionId())
                .setBillingAccountId(tx.getBillingAccountId().toString())
                .setPatientId(tx.getPatientId())
                .setType(tx.getType())
                .setAmount(tx.getAmount())
                .setStatus(tx.getStatus()) // COMPLETED
                .setCreatedAt(tx.getCreatedAt().toString())
                .build();

        try {
            kafkaTemplate.send("billing", event.toByteArray());
        } catch (Exception e) {
            log.error("Error sending Transaction event: {}", event);
        }
    }
}
