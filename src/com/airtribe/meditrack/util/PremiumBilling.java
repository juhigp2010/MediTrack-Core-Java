package com.airtribe.meditrack.util;

import com.airtribe.meditrack.interfaces.BillingStrategy;

public class PremiumBilling implements BillingStrategy {
    private final double premiumPercentage;

    public PremiumBilling(double premiumPercentage) {
        this.premiumPercentage = premiumPercentage;
    }

    @Override
    public double calculate(double baseAmount) {
        return baseAmount * (1.0 + premiumPercentage);
    }
}
