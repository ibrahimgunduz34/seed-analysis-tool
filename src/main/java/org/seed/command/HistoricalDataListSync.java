package org.seed.command;


import jakarta.transaction.Transactional;
import org.seed.fund.FundStorage;
import org.seed.fund.HistoricalDataService;
import org.seed.fund.model.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Map;
import java.util.concurrent.atomic.AtomicReference;
import java.util.function.Function;
import java.util.stream.Collectors;

@Component
@ConditionalOnProperty(name = "task", havingValue = "HistoricalDataListSync")
public class HistoricalDataListSync implements CommandLineRunner {
    private final Logger logger = LoggerFactory.getLogger(HistoricalDataListSync.class);

    private final HistoricalDataService historicalDataService;
    private final FundStorage fundStorage;

    public HistoricalDataListSync(HistoricalDataService historicalDataService, FundStorage fundStorage) {
        this.historicalDataService = historicalDataService;
        this.fundStorage = fundStorage;
    }

    @Override
    @Transactional
    public void run(String... args) throws Exception {
        logger.info("Starting HistoricalDataListSync job");

        LocalDate valueDate = LocalDate.now();

        ServiceResponse<List<ExternalHistoricalData>> providerResponse = historicalDataService.retrieveList(valueDate);

        List<ExternalHistoricalData> externalHistoricalData;
        try {
            externalHistoricalData = providerResponse.getData().orElseThrow(() -> new RuntimeException(providerResponse.getError()));
        } catch (Exception e) {
            logger.error("Provider data cannot be fetched. Error: " + providerResponse.getError());
            System.exit(1);
            return;
        }

        List<Fund> fundsByValueDate = fundStorage.getFundsByValueDate(valueDate);
        List<String> synchronizedFunds = fundsByValueDate.stream().map(item -> item.getMetaData().getCode()).toList();
        Map<String, MetaData> fundsMap = fundStorage.getMetaDataList().stream().collect(Collectors.toMap(MetaData::getCode, Function.identity()));

        AtomicReference<Integer> createdCount = new AtomicReference<>(0);
        externalHistoricalData.stream()
                .filter(item -> !synchronizedFunds.contains(item.getMetaData().getCode()))
                .collect(Collectors.groupingBy(item -> item.getMetaData().getCode()))
                .forEach((key, value) -> {
                    MetaData metaData = fundsMap.get(key);
                    List<HistoricalData> historicalData = value.stream().map(ExternalHistoricalData::toModel).toList();
                    fundStorage.saveAll(metaData, historicalData);
                    createdCount.set(createdCount.get() + 1);
                });

        logger.info("Created " + createdCount + " item(s) for " + valueDate.toString());
        logger.info("HistoricalDataListSync Job has been completed");
        System.exit(0);
    }
}
