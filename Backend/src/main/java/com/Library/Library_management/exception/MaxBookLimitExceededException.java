package com.Library.Library_management.exception;

public class MaxBookLimitExceededException extends RuntimeException {
    public MaxBookLimitExceededException(String message) {
        super(message);
    }
}
