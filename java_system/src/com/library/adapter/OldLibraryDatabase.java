package com.library.adapter;

import java.util.HashMap;
import java.util.Map;

/**
 * Incompatible legacy database system (Adaptee).
 * Returns raw String[] arrays instead of modern strongly-typed Book domain objects:
 * Index 0: ISBN
 * Index 1: Title
 * Index 2: Author
 * Index 3: Category
 * Index 4: Available Stock
 */
public class OldLibraryDatabase {
    // Incompatible internal storage returning raw String[] records
    private final Map<String, String[]> legacyDatabaseTable;

    public OldLibraryDatabase() {
        this.legacyDatabaseTable = new HashMap<>();

        // Populate with legacy enterprise inventory records
        legacyDatabaseTable.put("978-0132350884", new String[]{
                "978-0132350884", "Clean Code", "Robert C. Martin", "Programming", "1"
        });
        legacyDatabaseTable.put("978-0201633610", new String[]{
                "978-0201633610", "Design Patterns (GoF)", "Erich Gamma et al.", "Software Architecture", "2"
        });
        legacyDatabaseTable.put("978-0134685991", new String[]{
                "978-0134685991", "Effective Java (3rd Edition)", "Joshua Bloch", "Programming", "3"
        });
        legacyDatabaseTable.put("978-0596007126", new String[]{
                "978-0596007126", "Head First Design Patterns", "Eric Freeman", "Object-Oriented Design", "4"
        });
    }

    /**
     * Obsolete method returning unstructured raw String[] array.
     *
     * @param isbn Barcode/ISBN key
     * @return String[] record or null if not found
     */
    public String[] fetchRawRecord(String isbn) {
        System.out.println("[OLD DB] Fetching raw String[] record for ISBN: " + isbn);
        return legacyDatabaseTable.get(isbn);
    }

    public void insertRawRecord(String isbn, String[] record) {
        legacyDatabaseTable.put(isbn, record);
    }
}
