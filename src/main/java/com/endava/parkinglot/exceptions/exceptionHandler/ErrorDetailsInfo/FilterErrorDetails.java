package com.endava.parkinglot.exceptions.exceptionHandler.ErrorDetailsInfo;

public class FilterErrorDetails {

    private final String timestamp;
    private final String message;

    public FilterErrorDetails(String timestamp, String message) {
        this.timestamp = timestamp;
        this.message = message;
    }

    public String getTimestamp() {
        return timestamp;
    }

    public String getMessage() {
        return message;
    }
}
