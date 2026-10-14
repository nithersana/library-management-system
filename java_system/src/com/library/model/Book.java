package com.library.model;

/**
 * Domain entity representing a Book title and physical copy counts.
 */
public class Book {
    private String isbn;
    private String title;
    private String author;
    private String category;
    private int totalCopies;
    private int availableCopies;

    public Book(String isbn, String title, String author, String category, int totalCopies) {
        this.isbn = isbn;
        this.title = title;
        this.author = author;
        this.category = category;
        this.totalCopies = Math.max(0, totalCopies);
        this.availableCopies = this.totalCopies;
    }

    /**
     * Increments the total and available copy counts.
     */
    public synchronized void addCopy(int count) {
        if (count > 0) {
            this.totalCopies += count;
            this.availableCopies += count;
            System.out.println("[BOOK] Added " + count + " copies to '" + this.title + "'. Available: " + this.availableCopies);
        }
    }

    /**
     * Decrements copies when damaged or permanently decommissioned.
     */
    public synchronized void removeCopy(int count) {
        if (count > 0 && this.availableCopies >= count) {
            this.totalCopies -= count;
            this.availableCopies -= count;
            System.out.println("[BOOK] Removed " + count + " copies from '" + this.title + "'. Available: " + this.availableCopies);
        } else {
            System.err.println("[BOOK ERROR] Cannot remove " + count + " copies. Insufficient available copies (" + this.availableCopies + ").");
        }
    }

    /**
     * Checks if at least one copy is currently available for checkout.
     */
    public synchronized boolean isAvailable() {
        return this.availableCopies > 0;
    }

    /**
     * Internal method to decrement available copies on loan issue.
     */
    public synchronized boolean decrementAvailableCopies() {
        if (this.availableCopies > 0) {
            this.availableCopies--;
            return true;
        }
        return false;
    }

    /**
     * Internal method to increment available copies on return.
     */
    public synchronized void incrementAvailableCopies() {
        if (this.availableCopies < this.totalCopies) {
            this.availableCopies++;
        }
    }

    // Accessors and Mutators
    public String getIsbn() {
        return isbn;
    }

    public void setIsbn(String isbn) {
        this.isbn = isbn;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public int getTotalCopies() {
        return totalCopies;
    }

    public int getAvailableCopies() {
        return availableCopies;
    }

    @Override
    public String toString() {
        return String.format("Book [ISBN: %s, Title: \"%s\", Author: %s, Category: %s, Available: %d/%d]",
                isbn, title, author, category, availableCopies, totalCopies);
    }
}
