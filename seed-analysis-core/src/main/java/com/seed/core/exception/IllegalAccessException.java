package com.seed.core.exception;

public class IllegalAccessException extends RuntimeException {
    public IllegalAccessException(String message, Throwable cause) {
        super(message, cause);
    }

    public IllegalAccessException(String message) {
        super(message);
    }
}
