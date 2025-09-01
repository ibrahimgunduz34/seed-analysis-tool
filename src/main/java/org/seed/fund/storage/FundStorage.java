package org.seed.fund.storage;

import org.seed.fund.model.Fund;
import org.seed.fund.model.HistoricalData;
import org.seed.fund.model.MetaData;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;

public interface FundStorage {
    Fund getFundByCode(String code);
    List<MetaData> getMetaDataList();
    List<Fund> getFundsByValueDate(LocalDate valueDate);
    List<HistoricalData> getHistoricalDataByDateRange(String code, LocalDate beginDate, LocalDate endDate);

    MetaData save(MetaData metaData);
    void saveAll(List<MetaData> metaDataList);

    HistoricalData save(MetaData metaData, HistoricalData historicalData);
//    void saveAll(MetaData metaData, List<HistoricalData> historicalDataList);
    List<HistoricalData> saveAll(Map<String, List<HistoricalData>> models);
}
