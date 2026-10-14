package com.library.model;

import com.library.catalog.Catalog;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Concrete domain class representing a library borrower/member (Student, Faculty, Guest).
 */
public class Member extends User {
    private String membershipId;
    private int maxBooksAllowed;
    private final List<Loan> activeLoans;
    private final List<Loan> loanHistory;
    private final List<Book> reservedBooks;

    public Member(String userId, String name, String email, String password, String membershipId) {
        super(userId, name, email, password);
        this.membershipId = membershipId;
        this.maxBooksAllowed = 3; // Default limit per requirement
        this.activeLoans = new ArrayList<>();
        this.loanHistory = new ArrayList<>();
        this.reservedBooks = new ArrayList<>();
    }

    public Member(String userId, String name, String email, String password, String membershipId, int maxBooksAllowed) {
        super(userId, name, email, password);
        this.membershipId = membershipId;
        this.maxBooksAllowed = maxBooksAllowed;
        this.activeLoans = new ArrayList<>();
        this.loanHistory = new ArrayList<>();
        this.reservedBooks = new ArrayList<>();
    }

    /**
     * Searches books by title, author, category, or ISBN via the Catalog Singleton.
     */
    public List<Book> searchBook(String query) {
        System.out.println("[MEMBER] " + getName() + " searching catalog for query: \"" + query + "\"");
        return Catalog.getInstance().searchBook(query);
    }

    /**
     * Reserves a book when no copies are currently available.
     */
    public void reserveBook(Book book) {
        if (book != null && !reservedBooks.contains(book)) {
            reservedBooks.add(book);
            System.out.println("[MEMBER] " + getName() + " placed a hold/reservation on '" + book.getTitle() + "'.");
        }
    }

    /**
     * Displays borrowing history and current active loans.
     */
    public void viewHistory() {
        System.out.println("========== BORROWING HISTORY FOR " + getName() + " (" + membershipId + ") ==========");
        System.out.println("Active Loans (" + activeLoans.size() + "/" + maxBooksAllowed + "):");
        if (activeLoans.isEmpty()) {
            System.out.println("  (None)");
        } else {
            for (Loan l : activeLoans) {
                System.out.println("  - " + l.getBook().getTitle() + " [Due: " + l.getDueDate() + ", Loan ID: " + l.getLoanId() + "]");
            }
        }
        System.out.println("Past Returned Loans:");
        if (loanHistory.isEmpty()) {
            System.out.println("  (None)");
        } else {
            for (Loan l : loanHistory) {
                System.out.println("  - " + l.getBook().getTitle() + " [Returned: " + l.getReturnDate() + ", Status: " + l.getStatus() + "]");
            }
        }
        System.out.println("==============================================================");
    }

    public void addActiveLoan(Loan loan) {
        this.activeLoans.add(loan);
    }

    public void removeActiveLoan(Loan loan) {
        this.activeLoans.remove(loan);
        this.loanHistory.add(loan);
    }

    @Override
    public String getRole() {
        return "MEMBER";
    }

    // Accessors and Mutators
    public String getMembershipId() {
        return membershipId;
    }

    public void setMembershipId(String membershipId) {
        this.membershipId = membershipId;
    }

    public int getMaxBooksAllowed() {
        return maxBooksAllowed;
    }

    public void setMaxBooksAllowed(int maxBooksAllowed) {
        this.maxBooksAllowed = maxBooksAllowed;
    }

    public List<Loan> getActiveLoans() {
        return Collections.unmodifiableList(activeLoans);
    }

    public List<Loan> getLoanHistory() {
        return Collections.unmodifiableList(loanHistory);
    }

    public List<Book> getReservedBooks() {
        return Collections.unmodifiableList(reservedBooks);
    }
}
