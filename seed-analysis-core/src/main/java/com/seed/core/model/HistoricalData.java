package com.seed.core.model;

import java.util.List;

public record HistoricalData<T extends Candle>(
        List<T> candles
) {}
