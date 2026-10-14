package com.library.exception;

/**
 * Exception thrown when a book has zero available copies for borrowing.
 */
public class BookUnavailableException extends Exception {
    public BookUnavailableException(String message) {
        super(message);
    }
}
