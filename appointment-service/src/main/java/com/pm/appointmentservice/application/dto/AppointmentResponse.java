package com.pm.appointmentservice.application.dto;

import com.pm.appointmentservice.domain.AppointmentStatus;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
@Builder
public class AppointmentResponse {
    private String name;
    private String email;
    private String address;
    private String dateOfBirth;
    private String appointmentDate;
    private AppointmentStatus status;
}
