package com.example.vitals.exception;

import java.util.UUID;

// Custom exception for reading not found
public class ReadingNotFoundException extends RuntimeException {

    public ReadingNotFoundException(UUID readingId) {
        super("Reading not found with ID: " + readingId);
    }

    public ReadingNotFoundException(String msg, String id) {
        super(msg + " : " + id);
    }

}
