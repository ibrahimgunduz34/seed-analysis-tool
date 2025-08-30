package org.seed.fund.command;


import jakarta.transaction.Transactional;
import org.seed.fund.storage.FundStorage;
import org.seed.fund.service.HistoricalDataService;
import org.seed.fund.model.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.atomic.AtomicReference;
import java.util.function.Function;
import java.util.stream.Collectors;

@Component
@ConditionalOnProperty(name = "task", havingValue = "HistoricalDataListSyncAll")
public class HistoricalDataListSyncAll implements CommandLineRunner {
    private final Logger logger = LoggerFactory.getLogger(HistoricalDataListSyncAll.class);

    private final HistoricalDataService historicalDataService;
    private final FundStorage fundStorage;

    private static final Integer FETCH_POOL_SIZE=8;
    private static final Integer PERSISTENCE_POOL_SIZE=4;

    public HistoricalDataListSyncAll(HistoricalDataService historicalDataService, FundStorage fundStorage) {
        this.historicalDataService = historicalDataService;
        this.fundStorage = fundStorage;
    }

    @Override
    public void run(String... args) throws Exception {
        logger.info("Starting HistoricalDataListSync job");

//        LocalDate valueDate = LocalDate.now();
        LocalDate startDate = LocalDate.parse(args[0], DateTimeFormatter.ofPattern("yyyy-MM-dd"));
        LocalDate endDate = LocalDate.parse(args[1], DateTimeFormatter.ofPattern("yyyy-MM-dd"));

//        LocalDate startDate = LocalDate.parse("2020-08-29", DateTimeFormatter.ofPattern("yyyy-MM-dd"));
//        LocalDate endDate = LocalDate.parse("2021-08-29", DateTimeFormatter.ofPattern("yyyy-MM-dd"));
        List<ExternalHistoricalData> externalHistoricalData = collectHistoricalData(startDate, endDate);

        Map<LocalDate, List<ExternalHistoricalData>> groupedExternalHistoricalData = externalHistoricalData.stream()
                .collect(Collectors.groupingBy(ExternalHistoricalData::getValueDate));

        persistBatch(groupedExternalHistoricalData);

        logger.info("HistoricalDataListSync Job has been completed");
        System.exit(0);
    }

    private void persistBatch(Map<LocalDate, List<ExternalHistoricalData>> entries) {
        ExecutorService executor = Executors.newFixedThreadPool(PERSISTENCE_POOL_SIZE);
        List<Future<?>> futures = new ArrayList<>();

        for (Map.Entry<LocalDate, List<ExternalHistoricalData>> entry : entries.entrySet()) {
            futures.add(executor.submit(() -> persist(entry.getKey(), entry.getValue())));
        }

        for (Future<?> future : futures) {
            try {
                future.get();
            } catch (InterruptedException | ExecutionException e) {
                logger.info("Failed to execute thread");
            }
        }

    }

    private List<ExternalHistoricalData> collectHistoricalData(LocalDate startDate, LocalDate endDate) {
        ExecutorService executor = Executors.newFixedThreadPool(FETCH_POOL_SIZE);

        Map<LocalDate, Future<ServiceResponse<List<ExternalHistoricalData>>>> futures = new HashMap<>();

        List<ExternalHistoricalData> buffer = new ArrayList<>();

        for (LocalDate valueDate = startDate; valueDate.isBefore(endDate); valueDate = valueDate.plusDays(1)) {
            LocalDate _valueDate = valueDate;
            futures.put(_valueDate, executor.submit(() -> historicalDataService.retrieveList(_valueDate)));
        }

        for (Map.Entry<LocalDate, Future<ServiceResponse<List<ExternalHistoricalData>>>> entry : futures.entrySet()) {
            try {
                ServiceResponse<List<ExternalHistoricalData>> response = entry.getValue().get();
                response.getData().ifPresent(items -> {
                    logger.info("Collected " + items.size() + " item(s) for " + entry.getKey().toString());
                    buffer.addAll(items);
                });
            } catch (InterruptedException | ExecutionException  e) {
                logger.info("Failed to execute thread");
            }
        }

        return buffer;
    }

    @Transactional
    private void persist(LocalDate valueDate, List<ExternalHistoricalData> externalHistoricalData) {
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
    }
}
