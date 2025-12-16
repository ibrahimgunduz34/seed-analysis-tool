package com.seed.core.exception;

public class ExternalServiceConnectionFailure extends RuntimeException {
    public ExternalServiceConnectionFailure(String message) {
        super(message);
    }

    public ExternalServiceConnectionFailure(String message, Throwable cause) {
        super(message, cause);
    }
}
