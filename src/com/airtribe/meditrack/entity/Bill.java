package com.airtribe.meditrack.entity;

import com.airtribe.meditrack.constants.Constants;
import com.airtribe.meditrack.interfaces.BillingStrategy;
import com.airtribe.meditrack.interfaces.Payable;
import com.airtribe.meditrack.util.StandardBilling;

public class Bill implements Payable {
    private final String billId;
    private final Appointment appointment;
    private final double baseAmount;
    private boolean isPaid;
    private BillingStrategy billingStrategy;

    public Bill(Appointment appointment, double baseAmount, String billId) {
        this(appointment, baseAmount, billId, new StandardBilling());
    }

    public Bill(Appointment appointment, double baseAmount, String billId, BillingStrategy billingStrategy) {
        this.appointment = appointment;
        this.baseAmount = baseAmount;
        this.billId = billId;
        this.isPaid = false;
        this.billingStrategy = billingStrategy != null ? billingStrategy : new StandardBilling();
    }

    public String getBillId() {
        return billId;
    }

    public Appointment getAppointment() {
        return appointment;
    }

    public double getBaseAmount() {
        return baseAmount;
    }

    public boolean isPaid() {
        return isPaid;
    }

    public BillingStrategy getBillingStrategy() {
        return billingStrategy;
    }

    public void setBillingStrategy(BillingStrategy billingStrategy) {
        this.billingStrategy = billingStrategy;
    }

    @Override
    public double calculateFinalAmount() {
        double strategyAmount = billingStrategy.calculate(baseAmount);
        return strategyAmount + (strategyAmount * Constants.TAX_RATE);
    }

    @Override
    public void processPayment() {
        this.isPaid = true;
        System.out.println("Payment processed successfully for Bill ID: " + billId +
                           " | Total Paid: $" + String.format("%.2f", calculateFinalAmount()));
    }
}
