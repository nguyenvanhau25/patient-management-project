package com.pm.doctorservice.interfaces.client;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;
import java.util.UUID;

@FeignClient(name = "pharmacy-service", url = "${services.pharmacy.url}")
public interface PharmacyClient {
    @GetMapping("/pharmacy/medicines")
    List<Map<String, Object>> listMedicines();

    @PostMapping("/pharmacy/medicines")
    Map<String, Object> addMedicine(@RequestBody Map<String, Object> medicine);

    @PostMapping("/pharmacy/prescriptions")
    Map<String, Object> createPrescription(@RequestBody Map<String, Object> prescription);

    @GetMapping("/pharmacy/prescriptions/patient/{patientId}")
    List<Map<String, Object>> getPrescriptionByPatient(@PathVariable("patientId") UUID patientId);

    @PostMapping("/pharmacy/prescriptions/{id}/dispense")
    Map<String, Object> dispensePrescription(@PathVariable("id") UUID id);
}
