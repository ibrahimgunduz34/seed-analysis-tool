package com.seed.core.storage;

import com.seed.core.model.Candle;
import com.seed.core.model.HistoricalData;
import com.seed.core.model.MetaData;

import java.time.LocalDate;

public interface HistoricalDataStorage<M extends MetaData, C extends Candle> {
    HistoricalData<C> getHistoricalDataByDateRange(M metaData, LocalDate startDate, LocalDate endDate);
}
