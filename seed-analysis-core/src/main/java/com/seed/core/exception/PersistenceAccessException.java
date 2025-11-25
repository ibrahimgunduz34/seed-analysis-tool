package com.seed.core.exception;

public class PersistenceAccessException extends RuntimeException {
    public PersistenceAccessException(String message, Throwable cause) {
        super(message, cause);
    }

    public PersistenceAccessException(String message) {
        super(message);
    }
}
