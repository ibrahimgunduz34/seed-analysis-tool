package org.seed.fund.storage;

import org.seed.fund.model.Fund;
import org.seed.fund.model.FundHistoricalData;
import org.seed.fund.model.FundMetaData;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;

public interface FundStorage {
    Fund getFundByCode(String code);
    List<FundMetaData> getMetaDataList();
    List<Fund> getFundsByValueDate(LocalDate valueDate);
    List<FundHistoricalData> getHistoricalDataByDateRange(String code, LocalDate beginDate, LocalDate endDate);
    void saveAll(List<FundMetaData> fundMetaDataList);
    List<FundHistoricalData> saveAll(Map<String, List<FundHistoricalData>> models);
}
