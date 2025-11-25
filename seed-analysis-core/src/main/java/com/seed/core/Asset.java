package com.seed.core;

import java.util.List;

public interface Asset<M extends MetaData, H extends HistoricalData> {
    Long getId();
    M getMetaData();
    List<H> getHistoricalData();
}
