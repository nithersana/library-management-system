package com.library.controller;

import com.library.catalog.Catalog;
import com.library.exception.BookUnavailableException;
import com.library.exception.BorrowingLimitExceededException;
import com.library.model.Book;
import com.library.model.Fine;
import com.library.model.Loan;
import com.library.model.Member;
import com.library.strategy.FineStrategy;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * Controller orchestrator class coordinating borrowing transactions, limit verification,
 * inventory copy adjustments, and strategy-based fine assessments.
 */
public class LibraryController {
    private static final int DEFAULT_LOAN_PERIOD_DAYS = 14;

    private final Map<String, Loan> activeLoans;
    private final List<Loan> allLoansHistory;
    private final List<Fine> assessedFines;

    private final AtomicInteger loanIdGenerator;
    private final AtomicInteger fineIdGenerator;

    public LibraryController() {
        this.activeLoans = new HashMap<>();
        this.allLoansHistory = new ArrayList<>();
        this.assessedFines = new ArrayList<>();
        this.loanIdGenerator = new AtomicInteger(1000);
        this.fineIdGenerator = new AtomicInteger(500);
    }

    /**
     * Issues a book to a member, enforcing business constraints.
     *
     * @param member       The borrowing member
     * @param isbn         ISBN of the requested book
     * @param fineStrategy Dynamic strategy applied for fine calculations
     * @param issueDate    Date of checkout
     * @return Created Loan instance
     * @throws BorrowingLimitExceededException when member already holds max allowed books
     * @throws BookUnavailableException        when book is not in catalog or has 0 available copies
     */
    public synchronized Loan issueBook(Member member, String isbn, FineStrategy fineStrategy, LocalDate issueDate)
            throws BorrowingLimitExceededException, BookUnavailableException {

        if (member == null) {
            throw new IllegalArgumentException("Borrowing member cannot be null");
        }

        // 1. Enforce Member Borrowing Limit Check
        if (member.getActiveLoans().size() >= member.getMaxBooksAllowed()) {
            throw new BorrowingLimitExceededException(String.format(
                    "Borrowing limit exceeded for member '%s' (ID: %s). Currently holding %d of %d maximum allowed books.",
                    member.getName(), member.getMembershipId(), member.getActiveLoans().size(), member.getMaxBooksAllowed()
            ));
        }

        // 2. Locate Book in Central Catalog
        Book book = Catalog.getInstance().findBookByIsbn(isbn);
        if (book == null) {
            throw new BookUnavailableException("Book with ISBN " + isbn + " does not exist in library catalog.");
        }

        // 3. Enforce Book Availability Check
        if (!book.isAvailable()) {
            throw new BookUnavailableException(String.format(
                    "Book \"%s\" (ISBN: %s) is currently unavailable. All %d copies are on loan.",
                    book.getTitle(), book.getIsbn(), book.getTotalCopies()
            ));
        }

        // 4. Update Copy Availability (Decrement)
        boolean decremented = book.decrementAvailableCopies();
        if (!decremented) {
            throw new BookUnavailableException("Failed to secure available copy for \"" + book.getTitle() + "\".");
        }

        // 5. Generate Loan Transaction
        String loanId = "LN-" + loanIdGenerator.incrementAndGet();
        Loan loan = new Loan(loanId, book, member, issueDate, DEFAULT_LOAN_PERIOD_DAYS, fineStrategy);

        // 6. Link to Member and Internal State
        member.addActiveLoan(loan);
        activeLoans.put(loanId, loan);
        allLoansHistory.add(loan);

        System.out.println(String.format(
                "[CONTROLLER SUCCESS] Issued \"%s\" to %s (Loan ID: %s, Due: %s, Strategy: %s). Copies remaining: %d/%d",
                book.getTitle(), member.getName(), loanId, loan.getDueDate(), fineStrategy.getStrategyName(),
                book.getAvailableCopies(), book.getTotalCopies()
        ));

        return loan;
    }

    /**
     * Overload for issueBook using current system date.
     */
    public Loan issueBook(Member member, String isbn, FineStrategy fineStrategy)
            throws BorrowingLimitExceededException, BookUnavailableException {
        return issueBook(member, isbn, fineStrategy, LocalDate.now());
    }

    /**
     * Processes book return, recalculates copy availability, and computes fines via strategy.
     *
     * @param loanId     Unique loan transaction ID
     * @param returnDate Actual date of physical return
     * @return Assessed Fine object if overdue, or null if returned on time
     */
    public synchronized Fine returnBook(String loanId, LocalDate returnDate) {
        Loan loan = activeLoans.get(loanId);
        if (loan == null) {
            System.err.println("[CONTROLLER ERROR] Active loan not found for ID: " + loanId);
            return null;
        }

        Book book = loan.getBook();
        Member member = loan.getMember();

        // 1. Update Copy Availability (Increment)
        book.incrementAvailableCopies();

        // 2. Strategy-based Fine Calculation
        double fineAmount = loan.calculateFine(returnDate);
        Fine fine = null;

        if (fineAmount > 0.0) {
            String fineId = "FN-" + fineIdGenerator.incrementAndGet();
            fine = new Fine(fineId, loanId, fineAmount, returnDate);
            assessedFines.add(fine);
            System.out.println(String.format(
                    "[CONTROLLER OVERDUE ALERT] Book \"%s\" returned by %s was overdue! Fine assessed: Rs. %.2f [Fine ID: %s] using %s",
                    book.getTitle(), member.getName(), fineAmount, fineId, loan.getFineStrategy().getStrategyName()
            ));
        } else {
            System.out.println(String.format(
                    "[CONTROLLER RETURN SUCCESS] Book \"%s\" returned on time by %s. No fines assessed.",
                    book.getTitle(), member.getName()
            ));
        }

        // 3. Close the Loan
        loan.closeLoan(returnDate);
        member.removeActiveLoan(loan);
        activeLoans.remove(loanId);

        System.out.println(String.format(
                "[CONTROLLER INVENTORY] Available copies for \"%s\" restored to: %d/%d",
                book.getTitle(), book.getAvailableCopies(), book.getTotalCopies()
        ));

        return fine;
    }

    public List<Loan> getActiveLoans() {
        return new ArrayList<>(activeLoans.values());
    }

    public List<Fine> getAssessedFines() {
        return Collections.unmodifiableList(assessedFines);
    }
}
