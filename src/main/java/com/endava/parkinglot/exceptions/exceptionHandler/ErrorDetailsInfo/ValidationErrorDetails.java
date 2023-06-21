package com.endava.parkinglot.exceptions.exceptionHandler.ErrorDetailsInfo;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;

public class ValidationErrorDetails {

    private final LocalDate timestamp;
    private final Map<String, String> errors;
    private final String description;

    public ValidationErrorDetails(LocalDate timestamp, Map<String, String> errors, String description) {
        this.timestamp = timestamp;
        this.errors = errors;
        this.description = description;
    }

    public Map<String, String> getErrors() {
        return errors;
    }

    public LocalDate getTimestamp() {
        return timestamp;
    }


    public String getDescription() {
        return description;
    }
}
