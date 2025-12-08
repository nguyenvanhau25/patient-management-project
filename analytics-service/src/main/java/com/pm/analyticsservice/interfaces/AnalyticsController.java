package com.pm.analyticsservice.interfaces;

import com.pm.analyticsservice.application.service.AnalyticsService;
import com.pm.analyticsservice.application.service.BillingServiceImpl;
import com.pm.analyticsservice.domain.PatientAnalytics;
import com.pm.analyticsservice.infrastructure.exception.ApiResponse;
import com.pm.analyticsservice.infrastructure.exception.AppException;
import com.pm.analyticsservice.infrastructure.exception.ErrorCode;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping("/analytics")
@RequiredArgsConstructor
@Tag(name = "api for analytics")
public class AnalyticsController {
    private final AnalyticsService analyticsService;
    private final BillingServiceImpl billingService;

    @GetMapping("/patients/count")
    @PreAuthorize("hasAnyRole('USER','ADMIN')")
    @Operation(summary = "phân tích số bệnh nhân mới theo ngày")
    public ResponseEntity<ApiResponse<Long>> getPatientCount(@RequestParam String date) {
        LocalDate localDate;
        try {
            localDate = LocalDate.parse(date);
        } catch (Exception e) {
            throw new AppException(ErrorCode.INVALID_DATE_FORMAT);
        }

        long n = analyticsService.countPatientsByDate(localDate);

        if (n == 0) {
            throw new AppException(ErrorCode.PATIENT_NOT_FOUND, "Không có bệnh nhân đăng ký trong ngày này");
        }

        return ResponseEntity.ok(ApiResponse.<Long>builder()
                .code("SUCCESS")
                .message("Thành công")
                .result(n)
                .build());
    }

    @GetMapping("/patients")
    @PreAuthorize("hasAnyRole('USER','ADMIN')")
    @Operation(summary = "lấy danh sách bệnh nhân đăng kí mới theo ngày")
    public ResponseEntity<ApiResponse<List<PatientAnalytics>>> getPatients(@RequestParam String date) {
        LocalDate localDate;
        try {
            localDate = LocalDate.parse(date);
        } catch (Exception e) {
            throw new AppException(ErrorCode.INVALID_DATE_FORMAT);
        }

        List<PatientAnalytics> patients = analyticsService.getPatientsByDate(localDate);

        if (patients.isEmpty()) {
            throw new AppException(ErrorCode.PATIENT_NOT_FOUND, "Không có bệnh nhân đăng ký trong ngày này");
        }

        return ResponseEntity.ok(
                ApiResponse.<List<PatientAnalytics>>builder()
                        .code("SUCCESS")
                        .message("Thành công")
                        .result(patients)
                        .build()
        );
    }


    @GetMapping("/growth-rate")
    @PreAuthorize("hasAnyRole('USER','ADMIN')")
    @Operation(summary = "tỷ lệ tăng trưởng bệnh nhân")
    public ResponseEntity<ApiResponse<Double>> getDailyGrowthRate() {
        double rate = analyticsService.trackDailyGrowthRate();

        return ResponseEntity.ok(
                ApiResponse.<Double>builder()
                        .code("SUCCESS")
                        .message("Thành công")
                        .result(rate)
                        .build()
        );
    }
    @PreAuthorize("hasRole('ADMIN')")
    @Operation(summary = "Tổng doanh thu theo ngày")
    @GetMapping("/revenue")
    public ResponseEntity<Double> getTotalRevenue(@RequestParam String date) {
        return ResponseEntity.ok(
                billingService.getTotalRevenue(LocalDate.parse(date))
        );
    }

    @PreAuthorize("hasRole('ADMIN')")
    @Operation(summary = "Top patient chi tiêu nhiều nhất")
    @GetMapping("/top-patient")
    public ResponseEntity<List<Object[]>> topPatient(@RequestParam int limit) {
        return ResponseEntity.ok(
                billingService.getTopPatientSpending(limit)
        );
    }

    @PreAuthorize("hasRole('ADMIN')")
    @Operation(summary = "Tổng số giao dịch thành công")
    @GetMapping("/completed/count")
    public ResponseEntity<Long> countCompleted() {
        return ResponseEntity.ok(
                billingService.countCompletedTransactions()
        );
    }

    @PreAuthorize("hasRole('ADMIN')")
    @Operation(summary = "Doanh thu theo bệnh nhân")
    @GetMapping("/revenue/patient/{patientId}")
    public ResponseEntity<Double> revenueByPatient(@PathVariable String patientId) {
        return ResponseEntity.ok(
                billingService.getRevenueByPatientId(patientId)
        );
    }

}
