package com.vmoscalciuc.parkinglot.exceptions.exceptionHandler.ErrorDetailsInfo;

import java.time.LocalDate;

public class ErrorDetails {

    private final LocalDate timestamp;
    private final String message;
    private final String description;


    public ErrorDetails(LocalDate timestamp, String message, String description) {
        this.timestamp = timestamp;
        this.message = message;
        this.description = description;
    }

    public LocalDate getTimestamp() {
        return timestamp;
    }

    public String getMessage() {
        return message;
    }

    public String getDescription() {
        return description;
    }
}
