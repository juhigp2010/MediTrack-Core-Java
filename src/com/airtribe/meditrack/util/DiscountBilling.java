package com.airtribe.meditrack.util;

import com.airtribe.meditrack.interfaces.BillingStrategy;

public class DiscountBilling implements BillingStrategy {
    private final double discountPercentage;

    public DiscountBilling(double discountPercentage) {
        this.discountPercentage = discountPercentage;
    }

    @Override
    public double calculate(double baseAmount) {
        return baseAmount * (1.0 - discountPercentage);
    }
}
