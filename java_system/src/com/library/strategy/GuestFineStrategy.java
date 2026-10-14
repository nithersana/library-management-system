package com.library.strategy;

/**
 * Concrete fine strategy for Guests: Rs. 3.0 per overdue day.
 */
public class GuestFineStrategy implements FineStrategy {
    private static final double RATE_PER_DAY = 3.0;

    @Override
    public double calculateFine(long overdueDays) {
        return overdueDays > 0 ? overdueDays * RATE_PER_DAY : 0.0;
    }

    @Override
    public String getStrategyName() {
        return "Guest Fine Strategy (Rs. " + RATE_PER_DAY + "/day)";
    }
}
