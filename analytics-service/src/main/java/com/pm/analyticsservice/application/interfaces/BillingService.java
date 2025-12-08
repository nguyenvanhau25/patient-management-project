package com.pm.analyticsservice.application.interfaces;


import billing.events.BillingEvent;

import java.time.LocalDate;
import java.util.List;

public interface BillingService {

    void saveBillingEvent(BillingEvent event);

    //  DOANH THU THEO NGÀY (VẼ LINE CHART)
    Double getTotalRevenue(LocalDate date);

    //  TOP PATIENT CHI TIÊU NHIỀU NHẤT
    List<Object[]> getTopPatientSpending(int limit);

    //  TỔNG SỐ GIAO DỊCH THÀNH CÔNG
    Long countCompletedTransactions();

    // DOANH THU THEO PATIENT
    Double getRevenueByPatientId(String patientId);
}
