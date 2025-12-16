package com.seed.core.exception;

public class NoResourceFoundException extends RuntimeException {
    public NoResourceFoundException(String message, Throwable cause) {
        super(message, cause);
    }

    public NoResourceFoundException(String message) {
        super(message);
    }
}
