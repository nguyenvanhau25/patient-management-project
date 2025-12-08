package com.pm.analyticsservice.infrastructure.kafka;

import billing.events.BillingEvent;
import com.google.protobuf.InvalidProtocolBufferException;
import com.pm.analyticsservice.application.service.AnalyticsService;
import com.pm.analyticsservice.application.service.BillingServiceImpl;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;
import patient.events.PatientEvent;

@Service
@RequiredArgsConstructor
public class KafkaConsumer {
    private final AnalyticsService analyticsService;
    private final BillingServiceImpl billingService;
    private static final Logger log = LoggerFactory.getLogger(
            KafkaConsumer.class);

    @KafkaListener(topics = "patient", groupId = "analytics-service")
    public void consumeEvent(byte[] event) {
        try {

            try {
                PatientEvent patientEvent = PatientEvent.parseFrom(event);
                if ("PATIENT_CREATED".equals(patientEvent.getEventType())) {

                    analyticsService.savePatientEvent(patientEvent); // lưu vào db
                    log.info("Received Patient Event: [PatientId={},PatientName={},PatientEmail={}]",
                            patientEvent.getPatientId(),
                            patientEvent.getName(),
                            patientEvent.getEmail());
                }
            }catch (InvalidProtocolBufferException e) {
                log.error("Error deserializing event {}", e.getMessage());
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

    }
    @KafkaListener(topics = "billing", groupId = "analytics-group")
    public void listen(byte[] message) {
        try {
            BillingEvent event = BillingEvent.parseFrom(message);

            log.info("Received BillingEvent: transactionId={}, patientId={}, amount={}",
                    event.getTransactionId(),
                    event.getPatientId(),
                    event.getAmount()
            );

            //  Đẩy qua service để lưu DB
            billingService.saveBillingEvent(event);

        } catch (InvalidProtocolBufferException e) {
            log.error("Failed to parse BillingEvent from Kafka", e);
        }
    }
}
