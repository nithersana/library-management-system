package com.library.exception;

/**
 * Exception thrown when a member tries to borrow more books than their allowed quota.
 */
public class BorrowingLimitExceededException extends Exception {
    public BorrowingLimitExceededException(String message) {
        super(message);
    }
}
