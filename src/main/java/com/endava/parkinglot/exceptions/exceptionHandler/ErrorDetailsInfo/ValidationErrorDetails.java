package com.endava.parkinglot.exceptions.exceptionHandler.ErrorDetailsInfo;

import java.time.LocalDate;
import java.util.List;

public class ValidationErrorDetails {

    private final LocalDate timestamp;
    private final List<ValidationErrorObject> errors;
    private final String description;

    public ValidationErrorDetails(LocalDate timestamp, List<ValidationErrorObject> errors, String description) {
        this.timestamp = timestamp;
        this.errors = errors;
        this.description = description;
    }

    public LocalDate getTimestamp() {
        return timestamp;
    }

    public List<ValidationErrorObject> getErrors() {
        return errors;
    }

    public String getDescription() {
        return description;
    }
}
