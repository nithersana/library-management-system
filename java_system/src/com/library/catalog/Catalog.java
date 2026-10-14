package com.library.catalog;

import com.library.model.Book;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.stream.Collectors;

/**
 * Singleton design pattern managing the library book collection.
 * Maintains a thread-safe private List<Book> per architectural specification.
 */
public class Catalog {
    // Volatile instance for double-checked locking thread-safety
    private static volatile Catalog instance;

    // Strictly private List<Book> per specification
    private final List<Book> books;

    // Private constructor preventing external instantiation
    private Catalog() {
        this.books = new CopyOnWriteArrayList<>();
    }

    /**
     * Global access point for the Catalog Singleton instance.
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
     * Adds a Book to the catalog collection, or increments copies if already present.
     *
     * @param book Book entity to add
     */
    public synchronized void addBook(Book book) {
        if (book == null || book.getIsbn() == null) {
            throw new IllegalArgumentException("Book entity and ISBN cannot be null");
        }

        // Search for existing title in List<Book>
        for (Book existing : books) {
            if (existing.getIsbn().equalsIgnoreCase(book.getIsbn())) {
                existing.addCopy(book.getTotalCopies());
                System.out.println("[CATALOG] Incremented copies for existing title: \"" + existing.getTitle() + "\" [Total: " + existing.getTotalCopies() + "]");
                return;
            }
        }

        // Add new book to the List<Book>
        books.add(book);
        System.out.println("[CATALOG] Registered new title: \"" + book.getTitle() + "\" [ISBN: " + book.getIsbn() + ", Copies: " + book.getTotalCopies() + "]");
    }

    /**
     * Removes a Book from the catalog by ISBN.
     *
     * @param isbn Unique ISBN identifier
     * @return true if removed, false otherwise
     */
    public synchronized boolean removeBook(String isbn) {
        if (isbn == null) return false;
        for (Book b : books) {
            if (b.getIsbn().equalsIgnoreCase(isbn)) {
                books.remove(b);
                System.out.println("[CATALOG] Removed book: \"" + b.getTitle() + "\" from catalog.");
                return true;
            }
        }
        return false;
    }

    /**
     * Searches books by title, author, category, or ISBN (case-insensitive).
     *
     * @param query Search query keyword
     * @return List of matching Book entities
     */
    public List<Book> searchBook(String query) {
        if (query == null || query.trim().isEmpty()) {
            return new ArrayList<>(books);
        }
        String q = query.trim().toLowerCase();
        return books.stream()
                .filter(b -> b.getIsbn().toLowerCase().contains(q)
                        || b.getTitle().toLowerCase().contains(q)
                        || b.getAuthor().toLowerCase().contains(q)
                        || b.getCategory().toLowerCase().contains(q))
                .collect(Collectors.toList());
    }

    /**
     * Helper lookup to find a specific book by exact ISBN.
     */
    public Book findBookByIsbn(String isbn) {
        if (isbn == null) return null;
        for (Book b : books) {
            if (b.getIsbn().equalsIgnoreCase(isbn)) {
                return b;
            }
        }
        return null;
    }

    /**
     * Returns an unmodifiable view of all books in the catalog.
     */
    public List<Book> getAllBooks() {
        return Collections.unmodifiableList(books);
    }

    public int getTotalBooksCount() {
        return books.size();
    }
}
