package com.seed.core;

import java.util.HashMap;
import java.util.Objects;
import java.util.Optional;

public class AnalysisContext<T extends Candle> {
    private final HashMap<ResultKey<?>, Object> results = new HashMap<>();
    private final HistoricalData<T> historicalData;

    public AnalysisContext(HistoricalData<T> historicalData) {
        this.historicalData = historicalData;
    }

    @SuppressWarnings("unchecked")
    public <K>Optional<K> get(ResultKey<K> key) {
        return Optional.ofNullable((K) results.get(key));
    }

    public <K> boolean putIfAbsent(ResultKey<K> key, K value) {
        Objects.requireNonNull(key); Objects.requireNonNull(value);
        if (!key.type().isInstance(value)) {
            throw new IllegalArgumentException("Wrong type");
        }

        return results.putIfAbsent(key, value) == null;
    }

    public HistoricalData<T> getHistoricalData() {
        return historicalData;
    }
}
