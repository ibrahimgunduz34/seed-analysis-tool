package org.seed.fund.service.persistence;

import org.seed.fund.model.ExternalHistoricalData;
import org.seed.fund.model.Fund;
import org.seed.fund.model.HistoricalData;
import org.seed.fund.model.MetaData;
import org.seed.fund.service.provider.mapper.ProviderHistoricalDataMapper;
import org.seed.fund.storage.FundStorage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;
import java.util.concurrent.atomic.AtomicReference;
import java.util.function.Function;
import java.util.stream.Collectors;

@Component
public class HistoricalDataPersistenceImpl implements HistoricalDataPersistence {
    private final Logger logger = LoggerFactory.getLogger(HistoricalDataPersistenceImpl.class);

    private final FundStorage fundStorage;
    private final ProviderHistoricalDataMapper historicalDataMapper;

    public HistoricalDataPersistenceImpl(FundStorage fundStorage, ProviderHistoricalDataMapper historicalDataMapper) {
        this.fundStorage = fundStorage;
        this.historicalDataMapper = historicalDataMapper;
    }

    @Transactional
    @Override
    public void persist(LocalDate valueDate, List<ExternalHistoricalData> externalHistoricalData) {
        List<Fund> fundsByValueDate = fundStorage.getFundsByValueDate(valueDate);

        List<String> synchronizedFunds = fundsByValueDate.stream()
                .map(item -> item.getMetaData().getCode())
                .toList();

        Map<String, MetaData> fundsMap = fundStorage.getMetaDataList().stream()
                .collect(Collectors.toMap(MetaData::getCode, Function.identity()));

        AtomicReference<Integer> createdCount = new AtomicReference<>(0);
        externalHistoricalData.stream()
                .filter(item -> !synchronizedFunds.contains(item.getMetaData().getCode()))
                .collect(Collectors.groupingBy(item -> item.getMetaData().getCode()))
                .forEach((key, value) -> {
                    MetaData metaData = fundsMap.get(key);
                    List<HistoricalData> historicalData = value.stream().map(historicalDataMapper::toModel).toList();
                    fundStorage.saveAll(metaData, historicalData);
                    createdCount.set(createdCount.get() + 1);
                });

        logger.info("Created " + createdCount + " item(s) for " + valueDate.toString());
    }
}
