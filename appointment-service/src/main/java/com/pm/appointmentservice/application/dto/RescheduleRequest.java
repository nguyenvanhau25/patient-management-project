package com.pm.appointmentservice.application.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class RescheduleRequest {
    private String appointmentDate;
    private String startTime;
    private String endTime;
}
