package org.seed.fund.model;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.Optional;

@AllArgsConstructor
@Getter
public class ServiceResponse<T> {
    private final T data;
    private final String error;

    public Optional<T> getData() {
        return Optional.ofNullable(data);
    }
}
