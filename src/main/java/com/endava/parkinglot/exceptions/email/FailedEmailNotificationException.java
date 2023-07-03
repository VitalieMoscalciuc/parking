package com.endava.parkinglot.exceptions.email;

public class FailedEmailNotificationException extends RuntimeException{
    public FailedEmailNotificationException(String message) {
        super(message);
    }
}
