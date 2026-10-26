package com.example.demo.exception;

public class UserException extends RuntimeException {

    private final String errorCode;

    public UserException(String message) {
        super(message);
        this.errorCode = "USER_ERROR";
    }

    public UserException(String errorCode, String message) {
        super(message);
        this.errorCode = errorCode;
    }

    public String getErrorCode() {
        return errorCode;
    }
}
