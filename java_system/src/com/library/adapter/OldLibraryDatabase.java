package com.library.adapter;

import java.util.HashMap;
import java.util.Map;

/**
 * Incompatible legacy database system (Adaptee).
 * Uses an obsolete record format with barcode keys and delimiter-separated values:
 * "BARCODE#TITLE#AUTHOR#GENRE#STOCK"
 */
public class OldLibraryDatabase {
    private final Map<String, String> legacyRecords = new HashMap<>();

    public OldLibraryDatabase() {
        // Pre-populate with legacy library warehouse entries
        legacyRecords.put("978-0132350884", "978-0132350884#Clean Code#Robert C. Martin#Software Engineering#4");
        legacyRecords.put("978-0201633610", "978-0201633610#Design Patterns (GoF)#Gamma, Helm, Johnson, Vlissides#Architecture#2");
        legacyRecords.put("978-0134685991", "978-0134685991#Effective Java (3rd Edition)#Joshua Bloch#Programming#5");
        legacyRecords.put("978-0596007126", "978-0596007126#Head First Design Patterns#Eric Freeman#Object-Oriented Design#3");
    }

    /**
     * Legacy query method returning raw string record.
     */
    public String fetchLegacyRecord(String legacyKey) {
        System.out.println("[OLD DB] Executing low-level query for key: " + legacyKey);
        return legacyRecords.get(legacyKey);
    }

    public void insertLegacyRecord(String key, String rawData) {
        legacyRecords.put(key, rawData);
    }
}
