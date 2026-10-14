package com.library.model;

import java.time.LocalDate;

/**
 * Domain entity representing a penalty assessed on overdue loans.
 */
public class Fine {
    private String fineId;
    private String loanId;
    private double amount;
    private boolean paidStatus;
    private LocalDate fineDate;

    public Fine(String fineId, String loanId, double amount, LocalDate fineDate) {
        this.fineId = fineId;
        this.loanId = loanId;
        this.amount = Math.max(0.0, amount);
        this.paidStatus = false;
        this.fineDate = fineDate;
    }

    /**
     * Records settlement of the fine.
     */
    public void payFine() {
        if (!this.paidStatus) {
            this.paidStatus = true;
            System.out.println("[FINE] Fine " + fineId + " of Rs. " + amount + " has been paid successfully.");
        } else {
            System.out.println("[FINE] Fine " + fineId + " is already settled.");
        }
    }

    /**
     * Returns the outstanding or assessed amount.
     */
    public double calculateAmount() {
        return this.amount;
    }

    // Accessors and Mutators
    public String getFineId() {
        return fineId;
    }

    public String getLoanId() {
        return loanId;
    }

    public double getAmount() {
        return amount;
    }

    public void setAmount(double amount) {
        this.amount = amount;
    }

    public boolean isPaidStatus() {
        return paidStatus;
    }

    public LocalDate getFineDate() {
        return fineDate;
    }

    @Override
    public String toString() {
        return String.format("Fine [ID: %s, Loan: %s, Amount: Rs. %.2f, Paid: %s, Date: %s]",
                fineId, loanId, amount, paidStatus ? "YES" : "NO", fineDate);
    }
}
