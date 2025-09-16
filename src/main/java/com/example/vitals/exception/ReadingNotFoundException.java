package com.example.vitals.exception;

import java.util.UUID;

// Custom exception for reading not found
public class ReadingNotFoundException extends RuntimeException {
    public ReadingNotFoundException(String message) {
        super(message);
    }

    public ReadingNotFoundException(UUID readingId) {
        super("Reading not found with ID: " + readingId);
    }

    public ReadingNotFoundException(String patientId, String type) {
        super("No readings found for patient: " + patientId + " with type: " + type);
    }
}
