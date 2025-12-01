package com.seed.core;

public record ResultKey<T>(String name, Class<T> type) {
    public static <T> ResultKey<T> of(String name, Class<T> type) {
        return new ResultKey<>(name, type);
    }
}
