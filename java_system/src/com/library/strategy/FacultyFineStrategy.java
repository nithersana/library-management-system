package com.library.strategy;

/**
 * Concrete fine strategy for Faculty: Rs. 0.5 per overdue day.
 */
public class FacultyFineStrategy implements FineStrategy {
    private static final double RATE_PER_DAY = 0.5;

    @Override
    public double calculateFine(long overdueDays) {
        return overdueDays > 0 ? overdueDays * RATE_PER_DAY : 0.0;
    }

    @Override
    public String getStrategyName() {
        return "Faculty Fine Strategy (Rs. " + RATE_PER_DAY + "/day)";
    }
}
