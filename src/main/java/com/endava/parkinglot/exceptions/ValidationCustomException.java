package com.endava.parkinglot.exceptions;

import com.endava.parkinglot.exceptions.exceptionHandler.ErrorDetailsInfo.ValidationErrorObject;

import java.util.List;

public class ValidationCustomException extends RuntimeException {

    private final List<ValidationErrorObject> errorObjectList;

    public ValidationCustomException(List<ValidationErrorObject> errorObjectList) {
        this.errorObjectList = errorObjectList;
    }

    public List<ValidationErrorObject> getErrorObjectList() {
        return errorObjectList;
    }
}
