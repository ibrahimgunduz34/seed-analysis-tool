package com.seed.core;

import java.util.List;

public record HistoricalData<T extends Candle>(
        List<T> candles
) {}
