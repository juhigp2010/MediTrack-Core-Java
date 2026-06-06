package com.airtribe.meditrack.entity;

import com.airtribe.meditrack.interfaces.BillingStrategy;

public class ConsultationBill extends Bill {
    public ConsultationBill(Appointment appointment, double baseAmount, String billId) {
        super(appointment, baseAmount, billId);
    }

    public ConsultationBill(Appointment appointment, double baseAmount, String billId, BillingStrategy strategy) {
        super(appointment, baseAmount, billId, strategy);
    }

    @Override
    public void processPayment() {
        super.processPayment();
        System.out.println("Receipt: Consultation services for Doctor: " + getAppointment().getDoctor().getName());
    }
}
