package com.library.model;

import com.library.strategy.FineStrategy;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;

/**
 * Domain entity representing an active or completed borrowing transaction.
 * Demonstrates dynamic strategy injection for fine calculation.
 */
public class Loan {
    private String loanId;
    private Book book;
    private Member member;
    private LocalDate issueDate;
    private LocalDate dueDate;
    private LocalDate returnDate;
    private LoanStatus status;
    private FineStrategy fineStrategy;

    public Loan(String loanId, Book book, Member member, LocalDate issueDate, int loanPeriodDays, FineStrategy fineStrategy) {
        this.loanId = loanId;
        this.book = book;
        this.member = member;
        this.issueDate = issueDate;
        this.dueDate = issueDate.plusDays(loanPeriodDays);
        this.returnDate = null;
        this.status = LoanStatus.ACTIVE;
        this.fineStrategy = fineStrategy;
    }

    /**
     * Calculates the overdue fine dynamically using the associated FineStrategy.
     *
     * @param actualReturnDate The date the book is returned
     * @return Calculated fine amount in Rupees
     */
    public double calculateFine(LocalDate actualReturnDate) {
        if (actualReturnDate == null || !actualReturnDate.isAfter(this.dueDate)) {
            return 0.0;
        }
        long overdueDays = ChronoUnit.DAYS.between(this.dueDate, actualReturnDate);
        if (fineStrategy == null) {
            return 0.0;
        }
        return fineStrategy.calculateFine(overdueDays);
    }

    /**
     * Closes the loan upon physical return.
     *
     * @param actualReturnDate Return date
     */
    public void closeLoan(LocalDate actualReturnDate) {
        this.returnDate = actualReturnDate;
        if (actualReturnDate.isAfter(this.dueDate)) {
            this.status = LoanStatus.OVERDUE;
        } else {
            this.status = LoanStatus.RETURNED;
        }
        System.out.println("[LOAN] Loan " + loanId + " closed with status: " + this.status);
    }

    // Accessors and Mutators
    public String getLoanId() {
        return loanId;
    }

    public Book getBook() {
        return book;
    }

    public Member getMember() {
        return member;
    }

    public LocalDate getIssueDate() {
        return issueDate;
    }

    public LocalDate getDueDate() {
        return dueDate;
    }

    public LocalDate getReturnDate() {
        return returnDate;
    }

    public LoanStatus getStatus() {
        return status;
    }

    public void setStatus(LoanStatus status) {
        this.status = status;
    }

    public FineStrategy getFineStrategy() {
        return fineStrategy;
    }

    public void setFineStrategy(FineStrategy fineStrategy) {
        this.fineStrategy = fineStrategy;
    }

    @Override
    public String toString() {
        return String.format("Loan [ID: %s, Book: \"%s\", Member: %s, Issued: %s, Due: %s, Status: %s]",
                loanId, book.getTitle(), member.getName(), issueDate, dueDate, status);
    }
}
