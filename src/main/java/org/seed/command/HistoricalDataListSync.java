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
import java.util.concurrent.atomic.AtomicReference;

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

//        LocalDate valueDate = LocalDate.now();
        LocalDate valueDate = LocalDate.parse("2025-08-29", DateTimeFormatter.ofPattern("yyyy-MM-dd"));

        ServiceResponse<List<ExternalHistoricalData>> externalHistoricalDataList = historicalDataService.retrieveList(valueDate);
        List<Fund> fundsByValueDate = fundStorage.getFundsByValueDate(valueDate);
        List<String> fundCodes = fundsByValueDate.stream().map(item -> item.getMetaData().getCode()).toList();

        AtomicReference<Integer> createdCount = new AtomicReference<>(0);
        externalHistoricalDataList.getData().ifPresent(historicalDataList -> {
            historicalDataList.forEach(item -> {
                if (fundCodes.contains(item.getMetaData().getCode())) {
                    logger.warn("Historical data already exists for the specified value date. MetaData: " + item.getMetaData().getCode());
                    return;
                }
                fundStorage.save(item.getMetaData(), item.toModel());
                createdCount.set(createdCount.get() + 1);
            });
        });
        logger.info("Created " + createdCount.get() + " item(s).");
        logger.info("HistoricalDataListSync Job has been completed");
    }
}
