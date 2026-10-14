package com.library.adapter;

import com.library.model.Book;

/**
 * Target interface expected by modern library components to retrieve book data.
 */
public interface BookSource {
    /**
     * Retrieves and standardizes a Book instance by ISBN or identifier.
     */
    Book getBook(String isbn);
}
