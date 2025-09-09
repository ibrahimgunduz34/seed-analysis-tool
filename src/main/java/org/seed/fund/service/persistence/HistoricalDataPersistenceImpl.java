package org.seed.fund.service.persistence;

import org.seed.fund.mapper.FundHistoricalDataMapper;
import org.seed.fund.model.ExternalFundHistoricalData;
import org.seed.fund.model.Fund;
import org.seed.fund.model.FundHistoricalData;
import org.seed.fund.model.FundMetaData;
import org.seed.fund.storage.FundStorage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

@Component
public class HistoricalDataPersistenceImpl implements HistoricalDataPersistence {
    private final Logger logger = LoggerFactory.getLogger(HistoricalDataPersistenceImpl.class);

    private final FundStorage fundStorage;
    private final FundHistoricalDataMapper fundHistoricalDataMapper;

    public HistoricalDataPersistenceImpl(FundStorage fundStorage, FundHistoricalDataMapper fundHistoricalDataMapper) {
        this.fundStorage = fundStorage;
        this.fundHistoricalDataMapper = fundHistoricalDataMapper;
    }

    @Transactional
    @Override
    public void persist(LocalDate valueDate, List<ExternalFundHistoricalData> externalFundHistoricalData) {
        List<Fund> fundsByValueDate = fundStorage.getFundsByValueDate(valueDate);

        List<String> synchronizedFunds = fundsByValueDate.stream()
                .map(item -> item.getFundMetaData().getCode())
                .toList();

        Map<String, FundMetaData> fundsMap = fundStorage.getMetaDataList().stream()
                .collect(Collectors.toMap(FundMetaData::getCode, Function.identity()));

        Map<String, List<FundHistoricalData>> groupedHistoricalData = externalFundHistoricalData.stream()
                .filter(item -> !synchronizedFunds.contains(item.getFundMetaData().getCode()))
                .collect(Collectors.groupingBy(
                        item -> item.getFundMetaData().getCode(),
                        Collectors.mapping(fundHistoricalDataMapper::toModel, Collectors.toList())
                ));

        int createdCount = fundStorage.saveAll(groupedHistoricalData).size();

        logger.info("Created " + createdCount + " item(s) for " + valueDate.toString());
    }
}
