package com.library.strategy;

/**
 * Strategy interface defining the contract for calculating overdue fines.
 */
public interface FineStrategy {
    /**
     * Calculates fine based on number of overdue days.
     *
     * @param overdueDays Number of days past the loan due date
     * @return Total fine amount in Rupees
     */
    double calculateFine(long overdueDays);

    /**
     * Human-readable label for strategy reporting.
     */
    String getStrategyName();
}
