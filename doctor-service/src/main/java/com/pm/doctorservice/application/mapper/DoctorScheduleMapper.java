package com.pm.doctorservice.application.mapper;

import com.pm.doctorservice.application.dto.DoctorScheduleRequestDTO;
import com.pm.doctorservice.application.dto.DoctorScheduleResponseDTO;
import com.pm.doctorservice.domain.model.DoctorSchedule;

public class DoctorScheduleMapper {
    public static DoctorScheduleResponseDTO toDTO(DoctorSchedule schedule) {
        if (schedule == null) return null;
        DoctorScheduleResponseDTO dto = new DoctorScheduleResponseDTO();
        dto.setId(schedule.getId() != null ? schedule.getId().toString() : null);
        if (schedule.getDoctor() != null && schedule.getDoctor().getId() != null) {
            dto.setDoctorId(schedule.getDoctor().getId().toString());
        }
        dto.setWorkDate(schedule.getWorkDate());
        dto.setStartTime(schedule.getStartTime());
        dto.setEndTime(schedule.getEndTime());
        dto.setIsAvailable(schedule.getIsAvailable());
        return dto;
    }

    public static DoctorSchedule toModel(DoctorScheduleRequestDTO dto) {
        if (dto == null) return null;
        DoctorSchedule schedule = new DoctorSchedule();
        schedule.setWorkDate(dto.getWorkDate());
        schedule.setStartTime(dto.getStartTime());
        schedule.setEndTime(dto.getEndTime());
        schedule.setIsAvailable(dto.getIsAvailable() != null ? dto.getIsAvailable() : true);
        return schedule;
    }
}
