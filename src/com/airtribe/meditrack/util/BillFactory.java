package com.airtribe.meditrack.util;

import com.airtribe.meditrack.entity.*;
import com.airtribe.meditrack.interfaces.BillingStrategy;

public class BillFactory {
    public static Bill createBill(String type, Appointment appointment, double baseAmount, String billId) {
        if (type == null) {
            return new Bill(appointment, baseAmount, billId);
        }
        switch (type.toUpperCase()) {
            case "CONSULTATION":
                return new ConsultationBill(appointment, baseAmount, billId);
            case "PROCEDURE":
                return new ProcedureBill(appointment, baseAmount, billId);
            case "PHARMACY":
                return new PharmacyBill(appointment, baseAmount, billId);
            default:
                return new Bill(appointment, baseAmount, billId);
        }
    }

    public static Bill createBill(String type, Appointment appointment, double baseAmount, String billId, BillingStrategy strategy) {
        if (type == null) {
            return new Bill(appointment, baseAmount, billId, strategy);
        }
        switch (type.toUpperCase()) {
            case "CONSULTATION":
                return new ConsultationBill(appointment, baseAmount, billId, strategy);
            case "PROCEDURE":
                return new ProcedureBill(appointment, baseAmount, billId, strategy);
            case "PHARMACY":
                return new PharmacyBill(appointment, baseAmount, billId, strategy);
            default:
                return new Bill(appointment, baseAmount, billId, strategy);
        }
    }
}
