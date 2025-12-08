package com.seed.core;

import com.seed.core.model.Candle;
import com.seed.core.model.HistoricalData;
import com.seed.core.model.MetaData;
import com.seed.core.model.ResultKey;

import java.util.HashMap;
import java.util.Objects;
import java.util.Optional;

public class AnalysisContext<M extends MetaData, C extends Candle> {
    private final HashMap<ResultKey<?>, Object> results = new HashMap<>();
    private final M metaData;
    private final HistoricalData<C> historicalData;

    public AnalysisContext(M metaData, HistoricalData<C> historicalData) {
        this.metaData = metaData;
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

    public M getMetaData() { return metaData; }

    public HistoricalData<C> getHistoricalData() {
        return historicalData;
    }
}
