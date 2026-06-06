package com.airtribe.meditrack.entity;

public final class BillSummary {
    private final String billId;
    private final String patientName;
    private final double totalPaid;

    public BillSummary(String billId, String patientName, double totalPaid) {
        this.billId = billId;
        this.patientName = patientName;
        this.totalPaid = totalPaid;
    }

    public String getBillId() {
        return billId;
    }

    public String getPatientName() {
        return patientName;
    }

    public double getTotalPaid() {
        return totalPaid;
    }
}
