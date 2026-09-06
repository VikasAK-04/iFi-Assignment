package com.vikas.onefimarketplace.data.model;

import java.io.Serializable;
import java.util.Objects;

/**
 * Model representing an EMI financing plan option.
 */
public class EmiPlan implements Serializable {
    private final String id;
    private final int tenureMonths;
    private final double interestRate; // Annual percentage rate, e.g. 0.0 for 0% No Cost EMI
    private final double processingFee;
    private final boolean isNoCostEmi;
    private final String label;

    public EmiPlan(String id, int tenureMonths, double interestRate, double processingFee, boolean isNoCostEmi, String label) {
        this.id = id;
        this.tenureMonths = tenureMonths;
        this.interestRate = interestRate;
        this.processingFee = processingFee;
        this.isNoCostEmi = isNoCostEmi;
        this.label = label;
    }

    public String getId() {
        return id;
    }

    public int getTenureMonths() {
        return tenureMonths;
    }

    public double getInterestRate() {
        return interestRate;
    }

    public double getProcessingFee() {
        return processingFee;
    }

    public boolean isNoCostEmi() {
        return isNoCostEmi;
    }

    public String getLabel() {
        return label;
    }

    /**
     * Calculates the monthly payment for a given total product price.
     */
    public double calculateMonthlyAmount(double totalPrice) {
        if (tenureMonths <= 0) return totalPrice;
        if (isNoCostEmi || interestRate <= 0.0) {
            return (totalPrice + processingFee) / tenureMonths;
        } else {
            // Standard PMT formula: P * r * (1+r)^n / ((1+r)^n - 1)
            double monthlyRate = (interestRate / 100.0) / 12.0;
            double factor = Math.pow(1 + monthlyRate, tenureMonths);
            return (totalPrice * monthlyRate * factor) / (factor - 1);
        }
    }

    /**
     * Calculates the total amount payable over the tenure.
     */
    public double calculateTotalAmount(double totalPrice) {
        return calculateMonthlyAmount(totalPrice) * tenureMonths;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        EmiPlan emiPlan = (EmiPlan) o;
        return Objects.equals(id, emiPlan.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }
}
