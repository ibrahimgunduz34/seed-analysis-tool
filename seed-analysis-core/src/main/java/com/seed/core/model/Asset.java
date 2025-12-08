package com.seed.core.model;

public interface Asset<M extends MetaData, C extends Candle> {
    M getMetaData();
    HistoricalData<C> historicalData();
}
