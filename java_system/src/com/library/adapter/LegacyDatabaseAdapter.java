package com.library.adapter;

import com.library.model.Book;

/**
 * Adapter class implementing BookSource to bridge the modern Book domain model
 * with the obsolete OldLibraryDatabase string-based records.
 */
public class LegacyDatabaseAdapter implements BookSource {
    private final OldLibraryDatabase legacyDatabase;

    public LegacyDatabaseAdapter(OldLibraryDatabase legacyDatabase) {
        if (legacyDatabase == null) {
            throw new IllegalArgumentException("OldLibraryDatabase instance cannot be null");
        }
        this.legacyDatabase = legacyDatabase;
    }

    @Override
    public Book getBook(String isbn) {
        System.out.println("[ADAPTER] Translating request for ISBN " + isbn + " to legacy database format...");
        String rawRecord = legacyDatabase.fetchLegacyRecord(isbn);

        if (rawRecord == null || rawRecord.trim().isEmpty()) {
            System.err.println("[ADAPTER ERROR] No record found in legacy database for key: " + isbn);
            return null;
        }

        // Parse legacy format: "BARCODE#TITLE#AUTHOR#GENRE#STOCK"
        String[] tokens = rawRecord.split("#");
        if (tokens.length < 5) {
            System.err.println("[ADAPTER ERROR] Corrupted legacy format: " + rawRecord);
            return null;
        }

        String legacyIsbn = tokens[0].trim();
        String title = tokens[1].trim();
        String author = tokens[2].trim();
        String category = tokens[3].trim();
        int stock = 1;
        try {
            stock = Integer.parseInt(tokens[4].trim());
        } catch (NumberFormatException e) {
            System.err.println("[ADAPTER WARNING] Invalid stock value in legacy record. Defaulting to 1.");
        }

        Book adaptedBook = new Book(legacyIsbn, title, author, category, stock);
        System.out.println("[ADAPTER SUCCESS] Adapted legacy record into modern Book: " + adaptedBook);
        return adaptedBook;
    }
}
