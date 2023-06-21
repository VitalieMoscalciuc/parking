package com.endava.parkinglot.exceptions;

public class FailedEmailNotificationException extends RuntimeException{
    public FailedEmailNotificationException(String message) {
        super(message);
    }
}
