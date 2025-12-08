package com.seed.core.model;

import java.util.List;

public interface Asset<M extends MetaData, H extends HistoricalData> {
    M getMetaData();
    List<H> historicalData();
}
