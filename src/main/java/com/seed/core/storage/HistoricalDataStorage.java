package com.seed.core.storage;

import com.seed.core.model.Asset;
import com.seed.core.model.HistoricalData;
import com.seed.core.model.MetaData;
import com.seed.fund.model.Fund;

import java.time.LocalDate;
import java.util.List;

public interface HistoricalDataStorage<M extends MetaData, H extends HistoricalData> {
    List<H> getHistoricalDataByDateRange(M metaData, LocalDate startDate, LocalDate endDate);
    <T extends Asset<M, H>> void saveAll(List<T> fundList);
}
