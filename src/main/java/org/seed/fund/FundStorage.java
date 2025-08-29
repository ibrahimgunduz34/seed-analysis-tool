package org.seed.fund;

import org.seed.fund.model.Fund;
import org.seed.fund.model.HistoricalData;
import org.seed.fund.model.MetaData;

import java.time.LocalDate;
import java.util.List;

public interface FundStorage {
    Fund getFundByCode(String code);
    List<MetaData> getMetaDataList();
    List<HistoricalData> getHistoricalDataByDateRange(Fund fund, LocalDate beginDate, LocalDate endDate);
    List<Fund> getFundsByValueDate(LocalDate valueDate);

    MetaData save(MetaData metaData);
    void saveAll(List<MetaData> metaDataList);

    HistoricalData save(MetaData metaData, HistoricalData historicalData);
    void saveAll(MetaData metaData, List<HistoricalData> historicalDataList);
}
