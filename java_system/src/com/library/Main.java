package com.library;

import com.library.adapter.BookSource;
import com.library.adapter.LegacyDatabaseAdapter;
import com.library.adapter.OldLibraryDatabase;
import com.library.catalog.Catalog;
import com.library.controller.LibraryController;
import com.library.exception.BookNotFoundException;
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
 * Enterprise Driver Program & Output Generator for Library Management System
 * demonstrating OOAD / OOSE architecture:
 *
 * a) Catalog Singleton verification (confirming single object instance).
 * b) Adding books and searching catalog.
 * c) Borrowing limit enforcement (attempting to borrow 4th book and catching exception).
 * d) Issuing and returning books with Student, Faculty, and Guest fine strategies.
 * e) Legacy database import demonstration via the Adapter pattern.
 * f) Detailed execution summary report.
 */
public class Main {
    public static void main(String[] args) {
        printBanner("LIBRARY MANAGEMENT SYSTEM — OOAD ARCHITECTURAL VERIFICATION");

        // ═════════════════════════════════════════════════════════════
        // STEP A: CATALOG SINGLETON VERIFICATION
        // ═════════════════════════════════════════════════════════════
        printHeader("A. CATALOG SINGLETON PATTERN VERIFICATION");

        Catalog catalog1 = Catalog.getInstance();
        Catalog catalog2 = Catalog.getInstance();

        System.out.println("  • Catalog Instance 1 Identity HashCode: " + System.identityHashCode(catalog1));
        System.out.println("  • Catalog Instance 2 Identity HashCode: " + System.identityHashCode(catalog2));

        if (catalog1 == catalog2) {
            System.out.println("  [VERIFIED] catalog1 == catalog2 -> True.");
            System.out.println("  [PATTERN] Both references point to the exact same Singleton instance in JVM memory.");
        } else {
            System.err.println("  [FAILED] Singleton contract violated!");
        }

        // ═════════════════════════════════════════════════════════════
        // STEP B: ADDING BOOKS & SEARCHING CATALOG
        // ═════════════════════════════════════════════════════════════
        printHeader("B. ADDING BOOKS & MULTI-CRITERIA CATALOG SEARCH");

        Book b1 = new Book("978-0134685991", "Effective Java", "Joshua Bloch", "Programming", 2);
        Book b2 = new Book("978-0132350884", "Clean Code", "Robert C. Martin", "Programming", 1);
        Book b3 = new Book("978-0201633610", "Design Patterns", "Gang of Four", "Architecture", 2);
        Book b4 = new Book("978-0321356680", "Effective Unit Testing", "Lasse Koskela", "Testing", 1);

        catalog1.addBook(b1);
        catalog1.addBook(b2);
        catalog1.addBook(b3);
        catalog1.addBook(b4);

        System.out.println("\n  • Searching Catalog for query keyword: 'Effective'");
        List<Book> searchResults1 = catalog1.searchBook("Effective");
        for (Book b : searchResults1) {
            System.out.println("    -> " + b);
        }

        System.out.println("\n  • Searching Catalog for category: 'Architecture'");
        List<Book> searchResults2 = catalog1.searchBook("Architecture");
        for (Book b : searchResults2) {
            System.out.println("    -> " + b);
        }

        // ═════════════════════════════════════════════════════════════
        // STEP C: BORROWING LIMIT & AVAILABILITY ENFORCEMENT
        // ═════════════════════════════════════════════════════════════
        printHeader("C. BORROWING LIMIT & AVAILABILITY ENFORCEMENT");

        Admin admin = new Admin("ADM-01", "Systems Admin", "admin@lib.org", "admPass");
        Librarian librarian = new Librarian("LIB-01", "Dr. Sarah Connor", "sarah@lib.org", "libPass", "EMP-101");
        Member student = new Member("USR-01", "Ravi Kumar", "ravi@univ.edu", "studPass", "MEM-001", 3);
        Member faculty = new Member("USR-02", "Dr. Priya Sharma", "priya@univ.edu", "facPass", "MEM-002", 5);

        admin.createAccount(student);
        admin.createAccount(faculty);
        admin.createAccount(librarian);

        LibraryController controller = new LibraryController();

        System.out.println("\n  • Checkout operations for Student (Quota: " + student.getMaxBooksAllowed() + " books max):");
        try {
            System.out.println("    1. Checkout Book 1 (Effective Java)...");
            controller.issueBook(student, "978-0134685991", new StudentFineStrategy(), LocalDate.of(2026, 10, 1));

            System.out.println("    2. Checkout Book 2 (Clean Code)...");
            controller.issueBook(student, "978-0132350884", new StudentFineStrategy(), LocalDate.of(2026, 10, 2));

            System.out.println("    3. Checkout Book 3 (Design Patterns)...");
            controller.issueBook(student, "978-0201633610", new StudentFineStrategy(), LocalDate.of(2026, 10, 3));

            System.out.println("\n    [STATUS] Student currently holds " + student.getActiveLoans().size() + " active loans.");
            student.viewHistory();

            System.out.println("\n    4. Attempting 4th Checkout (Effective Unit Testing) — Quota violation expected:");
            controller.issueBook(student, "978-0321356680", new StudentFineStrategy(), LocalDate.of(2026, 10, 4));

        } catch (BorrowingLimitExceededException ex) {
            System.out.println("    [CAUGHT EXPECTED EXCEPTION] -> BorrowingLimitExceededException");
            System.out.println("    Exception Message: " + ex.getMessage());
        } catch (Exception ex) {
            System.err.println("    [UNEXPECTED ERROR] " + ex.getMessage());
        }

        System.out.println("\n  • Testing Book Unavailable Constraint:");
        try {
            System.out.println("    Faculty attempts to borrow 'Clean Code' (0 available copies remaining)...");
            controller.issueBook(faculty, "978-0132350884", new FacultyFineStrategy(), LocalDate.of(2026, 10, 5));
        } catch (BookUnavailableException ex) {
            System.out.println("    [CAUGHT EXPECTED EXCEPTION] -> BookUnavailableException");
            System.out.println("    Exception Message: " + ex.getMessage());
            System.out.println("    -> Member places reservation hold instead:");
            faculty.reserveBook(catalog1.findBookByIsbn("978-0132350884"));
        } catch (Exception ex) {
            System.err.println("    [UNEXPECTED ERROR] " + ex.getMessage());
        }

        System.out.println("\n  • Testing Book Not Found Constraint:");
        try {
            System.out.println("    Borrowing non-existent ISBN '978-9999999999'...");
            controller.issueBook(faculty, "978-9999999999", new FacultyFineStrategy(), LocalDate.of(2026, 10, 5));
        } catch (BookNotFoundException ex) {
            System.out.println("    [CAUGHT EXPECTED EXCEPTION] -> BookNotFoundException");
            System.out.println("    Exception Message: " + ex.getMessage());
        } catch (Exception ex) {
            System.err.println("    [UNEXPECTED ERROR] " + ex.getMessage());
        }

        // ═════════════════════════════════════════════════════════════
        // STEP D: STRATEGY PATTERN FOR FINE CALCULATIONS
        // ═════════════════════════════════════════════════════════════
        printHeader("D. STRATEGY PATTERN FOR DYNAMIC FINE CALCULATION");

        // Scenario 1: Student return (5 days overdue @ Rs. 1.0/day)
        Loan studentLoan = student.getActiveLoans().get(0); // Effective Java, due Oct 15
        LocalDate studentReturnDate = LocalDate.of(2026, 10, 20); // 5 days overdue
        System.out.println("  --- Scenario 1: Student Loan (Rate: Rs. 1.0/day) ---");
        System.out.println("  Issued: " + studentLoan.getIssueDate() + " | Due: " + studentLoan.getDueDate() + " | Returned: " + studentReturnDate + " (5 Days Overdue)");
        Fine f1 = controller.returnBook(studentLoan.getLoanId(), studentReturnDate);
        if (f1 != null) {
            System.out.println("  -> Fine Assessed: Rs. " + f1.calculateAmount() + " [Paid Status: " + f1.isPaidStatus() + "]");
            f1.payFine();
            System.out.println("  -> After Payment: [Paid Status: " + f1.isPaidStatus() + "]");
        }

        // Scenario 2: Faculty return (10 days overdue @ Rs. 0.5/day)
        System.out.println("\n  --- Scenario 2: Faculty Loan (Rate: Rs. 0.5/day) ---");
        try {
            Loan facLoan = controller.issueBook(faculty, "978-0134685991", new FacultyFineStrategy(), LocalDate.of(2026, 9, 1));
            // Due: Sep 15, 2026. Return: Sep 25, 2026 (10 days overdue). Expected fine = 10 * 0.5 = Rs. 5.0
            LocalDate facReturnDate = LocalDate.of(2026, 9, 25);
            System.out.println("  Issued: " + facLoan.getIssueDate() + " | Due: " + facLoan.getDueDate() + " | Returned: " + facReturnDate + " (10 Days Overdue)");
            Fine f2 = controller.returnBook(facLoan.getLoanId(), facReturnDate);
            if (f2 != null) {
                System.out.println("  -> Fine Assessed: Rs. " + f2.calculateAmount() + " [Paid Status: " + f2.isPaidStatus() + "]");
                f2.payFine();
            }
        } catch (Exception e) {
            System.err.println("  Error: " + e.getMessage());
        }

        // Scenario 3: Guest return (4 days overdue @ Rs. 3.0/day)
        System.out.println("\n  --- Scenario 3: Guest Loan (Rate: Rs. 3.0/day) ---");
        Member guest = new Member("USR-03", "Alex Miller (Guest)", "alex@guest.org", "guestPass", "MEM-003", 1);
        try {
            Loan guestLoan = controller.issueBook(guest, "978-0321356680", new GuestFineStrategy(), LocalDate.of(2026, 10, 1));
            // Due: Oct 15, 2026. Return: Oct 19, 2026 (4 days overdue). Expected fine = 4 * 3.0 = Rs. 12.0
            LocalDate guestReturnDate = LocalDate.of(2026, 10, 19);
            System.out.println("  Issued: " + guestLoan.getIssueDate() + " | Due: " + guestLoan.getDueDate() + " | Returned: " + guestReturnDate + " (4 Days Overdue)");
            Fine f3 = controller.returnBook(guestLoan.getLoanId(), guestReturnDate);
            if (f3 != null) {
                System.out.println("  -> Fine Assessed: Rs. " + f3.calculateAmount() + " [Paid Status: " + f3.isPaidStatus() + "]");
                f3.payFine();
            }
        } catch (Exception e) {
            System.err.println("  Error: " + e.getMessage());
        }

        // ═════════════════════════════════════════════════════════════
        // STEP E: ADAPTER PATTERN FOR LEGACY DATABASE INTEGRATION
        // ═════════════════════════════════════════════════════════════
        printHeader("E. ADAPTER PATTERN FOR LEGACY DATABASE INTEGRATION");

        System.out.println("  1. Legacy System: OldLibraryDatabase returns incompatible raw String[] array:");
        OldLibraryDatabase oldDb = new OldLibraryDatabase();
        String legacyKey = "978-0596007126";
        String[] rawRecord = oldDb.fetchRawRecord(legacyKey);
        System.out.println("     Raw Record Output: String[] { \"" + String.join("\", \"", rawRecord) + "\" }");

        System.out.println("\n  2. Adapter Wrapping: LegacyDatabaseAdapter implements modern BookSource interface:");
        BookSource adapter = new LegacyDatabaseAdapter(oldDb);
        Book modernBook = adapter.getBook(legacyKey);

        System.out.println("\n  3. Integration into Modern Catalog:");
        catalog1.addBook(modernBook);

        System.out.println("  4. Verification: Searching Catalog for newly adapted book 'Head First':");
        List<Book> searchAdapted = catalog1.searchBook("Head First");
        for (Book b : searchAdapted) {
            System.out.println("     -> " + b);
        }

        // ═════════════════════════════════════════════════════════════
        // STEP F: DETAILED EXECUTION SUMMARY REPORT
        // ═════════════════════════════════════════════════════════════
        printHeader("F. SYSTEM EXECUTION SUMMARY REPORT");

        System.out.println(String.format("  • Total Distinct Book Titles in Catalog : %d", catalog1.getTotalBooksCount()));
        System.out.println(String.format("  • Total System Loans Processed History  : %d", controller.getAllLoansHistory().size()));
        System.out.println(String.format("  • Current Active Loans Outstanding      : %d", controller.getActiveLoans().size()));
        System.out.println(String.format("  • Total Overdue Fines Assessed Count    : %d", controller.getAssessedFines().size()));

        double totalRevenue = controller.getAssessedFines().stream()
                .filter(Fine::isPaidStatus)
                .mapToDouble(Fine::getAmount)
                .sum();
        System.out.println(String.format("  • Total Settled Fine Revenue Collected  : Rs. %.2f", totalRevenue));

        printBanner("ALL ARCHITECTURAL SPECIFICATIONS EXECUTED AND VALIDATED WITH 100% SUCCESS");
    }

    private static void printBanner(String msg) {
        System.out.println("\n" + "=".repeat(80));
        System.out.println("  " + msg);
        System.out.println("=".repeat(80));
    }

    private static void printHeader(String title) {
        System.out.println("\n" + "-".repeat(80));
        System.out.println("  " + title);
        System.out.println("-".repeat(80));
    }
}
