package com.endava.parkinglot.exceptions;

import java.util.Map;

public class ValidationCustomException extends RuntimeException {

    private final Map<String, String> errorObjectMap;

    public ValidationCustomException(Map<String, String> errorObjectMap) {
        this.errorObjectMap = errorObjectMap;
    }

    public Map<String, String> getErrorObjectMap() {
        return errorObjectMap;
    }

}
