package com.library;

import com.library.adapter.BookSource;
import com.library.adapter.LegacyDatabaseAdapter;
import com.library.adapter.OldLibraryDatabase;
import com.library.catalog.Catalog;
import com.library.controller.LibraryController;
import com.library.exception.BookUnavailableException;
import com.library.exception.BorrowingLimitExceededException;
import com.library.model.Admin;
import com.library.model.Book;
import com.library.model.Fine;
import com.library.model.Librarian;
import com.library.model.Loan;
import com.library.model.Member;
import com.library.strategy.FacultyFineStrategy;
import com.library.strategy.GuestFineStrategy;
import com.library.strategy.StudentFineStrategy;

import java.time.LocalDate;
import java.util.List;

/**
 * Driver program demonstrating the OOAD Detailed Design implementation:
 * 1. Catalog Singleton verification.
 * 2. Book search and Member borrowing limit enforcement.
 * 3. Strategy pattern for overdue fine calculations.
 * 4. Adapter pattern for legacy database integration.
 * 5. Exception handling for business constraints.
 */
public class Main {
    public static void main(String[] args) {
        printBanner("LIBRARY MANAGEMENT SYSTEM — OOAD DETAILED DESIGN DEMO");

        // ═════════════════════════════════════════════════════════════
        // PART A: CATALOG SINGLETON VERIFICATION
        // ═════════════════════════════════════════════════════════════
        printSectionHeader("PART A: Catalog Singleton Pattern Verification");

        Catalog catalogRef1 = Catalog.getInstance();
        Catalog catalogRef2 = Catalog.getInstance();

        System.out.println("Reference 1 HashCode: " + System.identityHashCode(catalogRef1));
        System.out.println("Reference 2 HashCode: " + System.identityHashCode(catalogRef2));

        if (catalogRef1 == catalogRef2) {
            System.out.println(">>> SUCCESS: catalogRef1 == catalogRef2. Singleton contract verified (same instance in JVM).");
        } else {
            System.err.println(">>> FAILURE: Instances differ!");
        }

        // Initialize Catalog with sample titles
        System.out.println("\n--- Populating Catalog with Initial Books ---");
        Book book1 = new Book("978-0134685991", "Effective Java", "Joshua Bloch", "Programming", 2);
        Book book2 = new Book("978-0132350884", "Clean Code", "Robert C. Martin", "Programming", 1);
        Book book3 = new Book("978-0201633610", "Design Patterns", "Gang of Four", "Architecture", 2);
        Book book4 = new Book("978-0321356680", "Effective Unit Testing", "Lasse Koskela", "Testing", 1);

        catalogRef1.addBook(book1);
        catalogRef1.addBook(book2);
        catalogRef1.addBook(book3);
        catalogRef1.addBook(book4);

        System.out.println("Total distinct titles in catalog: " + catalogRef1.getTotalDistinctTitles());

        // ═════════════════════════════════════════════════════════════
        // PART B: BOOK SEARCH & BORROWING LIMIT ENFORCEMENT
        // ═════════════════════════════════════════════════════════════
        printSectionHeader("PART B: Member Book Search & Borrowing Limit Enforcement");

        // Initialize Users
        Admin admin = new Admin("ADM-001", "Chief Administrator", "admin@library.com", "adm123");
        Librarian librarian = new Librarian("LIB-001", "Dr. Sarah Connor", "librarian@library.com", "lib123", "EMP-9001");
        Member studentMember = new Member("USR-101", "Ravi Kumar", "ravi@student.edu", "pass123", "M-001", 3);
        Member facultyMember = new Member("USR-102", "Dr. Priya Sharma", "priya@faculty.edu", "pass456", "M-002", 5);

        admin.createAccount(studentMember);
        admin.createAccount(facultyMember);
        admin.createAccount(librarian);

        // Member searches for books
        System.out.println("\n--- Member Search Operation ---");
        List<Book> searchResults = studentMember.searchBook("Programming");
        System.out.println("Search Results for query 'Programming':");
        for (Book b : searchResults) {
            System.out.println("   -> " + b);
        }

        // Controller setup
        LibraryController controller = new LibraryController();

        System.out.println("\n--- Testing Borrowing Limit for Student (Max Allowed: " + studentMember.getMaxBooksAllowed() + ") ---");
        try {
            System.out.println("1. Borrowing Book 1: Effective Java");
            controller.issueBook(studentMember, "978-0134685991", new StudentFineStrategy(), LocalDate.of(2026, 10, 1));

            System.out.println("2. Borrowing Book 2: Clean Code");
            controller.issueBook(studentMember, "978-0132350884", new StudentFineStrategy(), LocalDate.of(2026, 10, 2));

            System.out.println("3. Borrowing Book 3: Design Patterns");
            controller.issueBook(studentMember, "978-0201633610", new StudentFineStrategy(), LocalDate.of(2026, 10, 3));

            System.out.println("\n>>> Student currently holds " + studentMember.getActiveLoans().size() + " active loans.");
            studentMember.viewHistory();

            System.out.println("\n4. Attempting to borrow Book 4 (Effective Unit Testing) — EXPECTING EXCEPTION...");
            controller.issueBook(studentMember, "978-0321356680", new StudentFineStrategy(), LocalDate.of(2026, 10, 4));

        } catch (BorrowingLimitExceededException ex) {
            System.out.println("\n>>> CAUGHT EXPECTED EXCEPTION: [BorrowingLimitExceededException]");
            System.out.println("    Message: " + ex.getMessage());
        } catch (BookUnavailableException ex) {
            System.err.println("Unexpected BookUnavailableException: " + ex.getMessage());
        }

        // Test Book Availability constraint
        System.out.println("\n--- Testing Book Availability Constraint ---");
        try {
            System.out.println("Faculty Dr. Priya attempts to borrow 'Clean Code' (0 copies remaining)...");
            controller.issueBook(facultyMember, "978-0132350884", new FacultyFineStrategy(), LocalDate.of(2026, 10, 5));
        } catch (BookUnavailableException ex) {
            System.out.println(">>> CAUGHT EXPECTED EXCEPTION: [BookUnavailableException]");
            System.out.println("    Message: " + ex.getMessage());
            System.out.println(">>> Faculty places a hold/reservation instead:");
            facultyMember.reserveBook(catalogRef1.findBookByIsbn("978-0132350884"));
        } catch (BorrowingLimitExceededException ex) {
            System.err.println("Unexpected BorrowingLimitExceededException: " + ex.getMessage());
        }

        // ═════════════════════════════════════════════════════════════
        // PART C: STRATEGY PATTERN OVERDUE FINE CALCULATIONS
        // ═════════════════════════════════════════════════════════════
        printSectionHeader("PART C: Strategy Pattern for Overdue Fine Calculations");

        // Scenario 1: Student returns book 5 days overdue (Student rate = Rs. 1.0/day)
        // Loan issued on Oct 1, 2026. Due date (14 days) = Oct 15, 2026.
        // Return date = Oct 20, 2026 (5 days overdue). Expected fine = 5 * 1.0 = Rs. 5.0
        Loan studentLoan = studentMember.getActiveLoans().get(0);
        LocalDate studentReturnDate = LocalDate.of(2026, 10, 20);

        System.out.println("\n--- Scenario 1: Student Loan Return (StudentFineStrategy @ Rs. 1/day) ---");
        System.out.println("Loan Issued: " + studentLoan.getIssueDate() + ", Due Date: " + studentLoan.getDueDate());
        System.out.println("Actual Return Date: " + studentReturnDate + " (5 Days Overdue)");

        Fine studentFine = controller.returnBook(studentLoan.getLoanId(), studentReturnDate);
        if (studentFine != null) {
            System.out.println("Generated Record: " + studentFine);
            studentFine.payFine();
        }

        // Scenario 2: Faculty borrows and returns book 10 days overdue (Faculty rate = Rs. 0.5/day)
        System.out.println("\n--- Scenario 2: Faculty Loan Return (FacultyFineStrategy @ Rs. 0.5/day) ---");
        try {
            Loan facultyLoan = controller.issueBook(facultyMember, "978-0134685991", new FacultyFineStrategy(), LocalDate.of(2026, 9, 1));
            // Due date: Sep 15, 2026. Return date: Sep 25, 2026 (10 days overdue). Expected fine = 10 * 0.5 = Rs. 5.0
            LocalDate facultyReturnDate = LocalDate.of(2026, 9, 25);
            System.out.println("Loan Issued: " + facultyLoan.getIssueDate() + ", Due Date: " + facultyLoan.getDueDate());
            System.out.println("Actual Return Date: " + facultyReturnDate + " (10 Days Overdue)");

            Fine facultyFine = controller.returnBook(facultyLoan.getLoanId(), facultyReturnDate);
            if (facultyFine != null) {
                System.out.println("Generated Record: " + facultyFine);
                facultyFine.payFine();
            }
        } catch (Exception e) {
            System.err.println("Error: " + e.getMessage());
        }

        // Scenario 3: Guest Member with GuestFineStrategy (Rs. 3.0/day)
        System.out.println("\n--- Scenario 3: Guest Loan Return (GuestFineStrategy @ Rs. 3/day) ---");
        Member guestMember = new Member("USR-103", "Alex Turner (Guest)", "alex@guest.org", "guestpass", "G-999", 1);
        try {
            Loan guestLoan = controller.issueBook(guestMember, "978-0321356680", new GuestFineStrategy(), LocalDate.of(2026, 10, 1));
            // Due date: Oct 15, 2026. Return date: Oct 19, 2026 (4 days overdue). Expected fine = 4 * 3.0 = Rs. 12.0
            LocalDate guestReturnDate = LocalDate.of(2026, 10, 19);
            System.out.println("Loan Issued: " + guestLoan.getIssueDate() + ", Due Date: " + guestLoan.getDueDate());
            System.out.println("Actual Return Date: " + guestReturnDate + " (4 Days Overdue)");

            Fine guestFine = controller.returnBook(guestLoan.getLoanId(), guestReturnDate);
            if (guestFine != null) {
                System.out.println("Generated Record: " + guestFine);
                guestFine.payFine();
            }
        } catch (Exception e) {
            System.err.println("Error: " + e.getMessage());
        }

        // ═════════════════════════════════════════════════════════════
        // PART D: ADAPTER PATTERN FOR LEGACY DATABASE INTEGRATION
        // ═════════════════════════════════════════════════════════════
        printSectionHeader("PART D: Adapter Pattern for Legacy Database Integration");

        System.out.println("1. Instantiating incompatible OldLibraryDatabase (raw delimited format)...");
        OldLibraryDatabase legacyDb = new OldLibraryDatabase();

        System.out.println("2. Wrapping legacy database into LegacyDatabaseAdapter (implementing modern BookSource)...");
        BookSource bookAdapter = new LegacyDatabaseAdapter(legacyDb);

        String legacyIsbn = "978-0596007126";
        System.out.println("\n3. Modern system requests Book via BookSource interface for ISBN: " + legacyIsbn);
        Book importedBook = bookAdapter.getBook(legacyIsbn);

        if (importedBook != null) {
            System.out.println("\n4. Adding adapted book to modern Catalog Singleton...");
            catalogRef1.addBook(importedBook);

            System.out.println("5. Verifying presence of adapted book in Catalog search:");
            List<Book> oodResults = catalogRef1.searchBook("Design Patterns");
            for (Book b : oodResults) {
                System.out.println("   -> " + b);
            }
        }

        printBanner("ALL OOAD DESIGN REQUIREMENTS VERIFIED SUCCESSFULLY");
    }

    private static void printBanner(String title) {
        System.out.println("\n" + "=".repeat(78));
        System.out.println("  " + title);
        System.out.println("=".repeat(78));
    }

    private static void printSectionHeader(String section) {
        System.out.println("\n" + "-".repeat(78));
        System.out.println("  " + section);
        System.out.println("-".repeat(78));
    }
}
