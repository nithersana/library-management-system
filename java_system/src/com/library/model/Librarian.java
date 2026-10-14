package com.library.model;

import com.library.catalog.Catalog;

/**
 * Concrete domain class representing a Librarian staff member.
 */
public class Librarian extends User {
    private String employeeId;

    public Librarian(String userId, String name, String email, String password, String employeeId) {
        super(userId, name, email, password);
        this.employeeId = employeeId;
    }

    /**
     * Adds or updates a book in the central catalog.
     */
    public void manageBook(Book book) {
        Catalog.getInstance().addBook(book);
        System.out.println("[LIBRARIAN " + employeeId + "] Managed book record: " + book.getTitle());
    }

    /**
     * Helper delegation for issuing book.
     */
    public void issueBook(Member member, Book book) {
        System.out.println("[LIBRARIAN " + employeeId + "] Processing checkout of '" + book.getTitle() + "' for " + member.getName());
    }

    /**
     * Helper delegation for returning book.
     */
    public void returnBook(Loan loan) {
        System.out.println("[LIBRARIAN " + employeeId + "] Processing check-in for Loan: " + loan.getLoanId());
    }

    @Override
    public String getRole() {
        return "LIBRARIAN";
    }

    // Accessors and Mutators
    public String getEmployeeId() {
        return employeeId;
    }

    public void setEmployeeId(String employeeId) {
        this.employeeId = employeeId;
    }
}
