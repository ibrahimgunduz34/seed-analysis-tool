package com.seed.core.model;

import java.util.Optional;

public class ServiceResponse<T> {
    private final T data;
    private final String error;

    public ServiceResponse(T data, String error) {
        this.data = data;
        this.error = error;
    }

    public Optional<T> getData() {
        return Optional.ofNullable(data);
    }

    public Optional<String> getError() {
        return Optional.ofNullable(error);
    }
}
