package com.pm.appointmentservice.application.dto;

import lombok.Data;
import java.util.UUID;

@Data
public class AppointmentRequest {
    private UUID patientId;
    private String appointmentDate;
}
