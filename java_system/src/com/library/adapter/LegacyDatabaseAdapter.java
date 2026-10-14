package com.library.adapter;

import com.library.model.Book;

/**
 * Adapter pattern implementation:
 * Bridges the modern BookSource interface with the legacy OldLibraryDatabase returning raw String[].
 */
public class LegacyDatabaseAdapter implements BookSource {
    private final OldLibraryDatabase legacyDatabase;

    public LegacyDatabaseAdapter(OldLibraryDatabase legacyDatabase) {
        if (legacyDatabase == null) {
            throw new IllegalArgumentException("OldLibraryDatabase dependency cannot be null");
        }
        this.legacyDatabase = legacyDatabase;
    }

    @Override
    public Book getBook(String isbn) {
        System.out.println("[ADAPTER] Adapting request for ISBN: " + isbn);
        String[] rawRecord = legacyDatabase.fetchRawRecord(isbn);

        if (rawRecord == null || rawRecord.length < 5) {
            System.err.println("[ADAPTER ERROR] Missing or corrupted legacy record for ISBN: " + isbn);
            return null;
        }

        // Map String[] elements to modern Book entity
        String legacyIsbn = rawRecord[0];
        String title = rawRecord[1];
        String author = rawRecord[2];
        String category = rawRecord[3];
        int copies = 1;
        try {
            copies = Integer.parseInt(rawRecord[4]);
        } catch (NumberFormatException e) {
            System.err.println("[ADAPTER WARNING] Non-integer copy count: " + rawRecord[4] + ". Defaulting to 1.");
        }

        Book adaptedBook = new Book(legacyIsbn, title, author, category, copies);
        System.out.println("[ADAPTER] Successfully adapted raw String[] into modern Book: " + adaptedBook);
        return adaptedBook;
    }
}
