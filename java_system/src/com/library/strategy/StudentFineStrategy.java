package com.library.strategy;

/**
 * Concrete fine strategy for Students: Rs. 1.0 per overdue day.
 */
public class StudentFineStrategy implements FineStrategy {
    private static final double RATE_PER_DAY = 1.0;

    @Override
    public double calculateFine(long overdueDays) {
        return overdueDays > 0 ? overdueDays * RATE_PER_DAY : 0.0;
    }

    @Override
    public String getStrategyName() {
        return "Student Fine Strategy (Rs. " + RATE_PER_DAY + "/day)";
    }
}
