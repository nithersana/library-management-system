package com.library.catalog;

import com.library.model.Book;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;

/**
 * Thread-safe Singleton managing the central library book collection.
 */
public class Catalog {
    // Volatile instance for double-checked locking pattern
    private static volatile Catalog instance;

    // In-memory thread-safe storage keyed by ISBN
    private final Map<String, Book> booksByIsbn;

    // Private constructor preventing direct instantiation
    private Catalog() {
        this.booksByIsbn = new ConcurrentHashMap<>();
    }

    /**
     * Returns the global Singleton instance of the Catalog.
     */
    public static Catalog getInstance() {
        if (instance == null) {
            synchronized (Catalog.class) {
                if (instance == null) {
                    instance = new Catalog();
                }
            }
        }
        return instance;
    }

    /**
     * Adds a new book or increments copies of an existing title.
     */
    public void addBook(Book book) {
        if (book == null || book.getIsbn() == null) {
            throw new IllegalArgumentException("Book and ISBN cannot be null");
        }
        if (booksByIsbn.containsKey(book.getIsbn())) {
            Book existing = booksByIsbn.get(book.getIsbn());
            existing.addCopy(book.getTotalCopies());
            System.out.println("[CATALOG] Incremented copies for existing title: " + existing.getTitle());
        } else {
            booksByIsbn.put(book.getIsbn(), book);
            System.out.println("[CATALOG] Registered new book: \"" + book.getTitle() + "\" [ISBN: " + book.getIsbn() + "]");
        }
    }

    /**
     * Removes a book completely from catalog by ISBN.
     */
    public boolean removeBook(String isbn) {
        if (isbn != null && booksByIsbn.containsKey(isbn)) {
            Book removed = booksByIsbn.remove(isbn);
            System.out.println("[CATALOG] Removed book: \"" + removed.getTitle() + "\" from catalog.");
            return true;
        }
        return false;
    }

    /**
     * Searches books across ISBN, Title, Author, or Category (case-insensitive).
     */
    public List<Book> searchBook(String query) {
        if (query == null || query.trim().isEmpty()) {
            return new ArrayList<>(booksByIsbn.values());
        }
        String lowerQuery = query.trim().toLowerCase();
        return booksByIsbn.values().stream()
                .filter(b -> b.getIsbn().toLowerCase().contains(lowerQuery)
                        || b.getTitle().toLowerCase().contains(lowerQuery)
                        || b.getAuthor().toLowerCase().contains(lowerQuery)
                        || b.getCategory().toLowerCase().contains(lowerQuery))
                .collect(Collectors.toList());
    }

    /**
     * Direct lookup by exact ISBN.
     */
    public Book findBookByIsbn(String isbn) {
        return isbn != null ? booksByIsbn.get(isbn) : null;
    }

    public List<Book> getAllBooks() {
        return new ArrayList<>(booksByIsbn.values());
    }

    public int getTotalDistinctTitles() {
        return booksByIsbn.size();
    }
}
