package com.airtribe.meditrack.util;

import com.airtribe.meditrack.interfaces.BillingStrategy;

public class StandardBilling implements BillingStrategy {
    @Override
    public double calculate(double baseAmount) {
        return baseAmount;
    }
}
