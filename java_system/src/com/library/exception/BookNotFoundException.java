package com.library.exception;

/**
 * Exception thrown when a book with the requested identifier cannot be located in the library catalog.
 */
public class BookNotFoundException extends Exception {
    public BookNotFoundException(String message) {
        super(message);
    }
}
