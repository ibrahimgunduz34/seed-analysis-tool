package org.seed.fund.service;

import org.seed.exception.ExternalServiceConnectionFailure;
import org.seed.fund.command.HistoricalDataListSyncAll;
import org.seed.fund.model.ExternalHistoricalData;
import org.seed.fund.model.ServiceResponse;
import org.seed.fund.service.persistence.HistoricalDataPersistence;
import org.seed.fund.service.provider.HistoricalDataService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.CompletionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.function.BiFunction;
import java.util.stream.Collectors;

@Component
public class HistoricalDataSynchronizerImpl implements HistoricalDataSynchronizer {
    private final Logger logger = LoggerFactory.getLogger(HistoricalDataListSyncAll.class);

    private final HistoricalDataService historicalDataService;
    private final HistoricalDataPersistence historicalDataPersistence;

    private static final Integer FETCH_POOL_SIZE = 3;
    private static final Integer PERSISTENCE_POOL_SIZE = 4;

    public HistoricalDataSynchronizerImpl(HistoricalDataService historicalDataService, HistoricalDataPersistence historicalDataPersistence) {
        this.historicalDataService = historicalDataService;
        this.historicalDataPersistence = historicalDataPersistence;
    }

    @Override
    public void synchronize(LocalDate valueDate) {
        List<ExternalHistoricalData> externalHistoricalData = fetchData(valueDate);

        if (externalHistoricalData.isEmpty()) {
            logger.info("Historical data is already in sync");
            return;
        }

        historicalDataPersistence.persist(valueDate, externalHistoricalData);
    }

    @Override
    public void synchronize(LocalDate beginDate, LocalDate endDate) {
        List<ExternalHistoricalData> externalHistoricalData = collectHistoricalData(beginDate, endDate);

        if (externalHistoricalData.isEmpty()) {
            logger.info("Historical data is already in sync");
            return;
        }

        Map<LocalDate, List<ExternalHistoricalData>> groupedExternalHistoricalData = externalHistoricalData.stream()
                .collect(Collectors.groupingBy(ExternalHistoricalData::getValueDate));

        persistBatch(groupedExternalHistoricalData);
    }

    private List<ExternalHistoricalData> collectHistoricalData(LocalDate beginDate, LocalDate endDate) {
        ExecutorService executor = Executors.newFixedThreadPool(FETCH_POOL_SIZE);

        try {
            List<CompletableFuture<List<ExternalHistoricalData>>> futures = new ArrayList<>();

            for (LocalDate valueDate = beginDate; !valueDate.isAfter(endDate); valueDate = valueDate.plusDays(1)) {
                final LocalDate requestDate = valueDate;

                CompletableFuture<List<ExternalHistoricalData>> future = CompletableFuture
                        .supplyAsync(() -> {
                            try {
                                Thread.sleep(800); // 500ms delay before calling fetchData
                            } catch (InterruptedException e) {
                                Thread.currentThread().interrupt();
                            };
                            return fetchData(requestDate);
                        }, executor)
                        .handle(handleBatchProviderResponse(requestDate));

                futures.add(future);
            }

            // join all futures and flatten successful results
            return futures.stream()
                    .map(CompletableFuture::join) // now never throws
                    .flatMap(List::stream)
                    .toList();

        } finally {
            executor.shutdown();
        }
    }

    private BiFunction<List<ExternalHistoricalData>, Throwable, List<ExternalHistoricalData>> handleBatchProviderResponse(LocalDate requestDate) {
        return (items, ex) -> {
            if (ex != null) {
                // unwrap CompletionException to get the real cause
                Throwable cause = ex instanceof CompletionException ? ex.getCause() : ex;

                if (cause instanceof ExternalServiceConnectionFailure) {
                    logger.error("Failed to fetch historical data for {}: {}", requestDate, cause.getMessage());
                } else {
                    logger.error("Unexpected error fetching historical data for {}: {}", requestDate, cause.toString());
                }
                // return empty list for this failed call
                return List.<ExternalHistoricalData>of();
            } else {
                logger.info("Collected {} item(s) for {}", items.size(), requestDate);
                return items;
            }
        };
    }


    private List<ExternalHistoricalData> fetchData(LocalDate valueDate) {
        ServiceResponse<List<ExternalHistoricalData>> providerResponse = historicalDataService.retrieveList(valueDate);

        return providerResponse.getData()
                .orElseThrow(() -> new ExternalServiceConnectionFailure(providerResponse.getError()));
    }

    @SuppressWarnings("resource")
    private void persistBatch(Map<LocalDate, List<ExternalHistoricalData>> entries) {
        ExecutorService executor = Executors.newFixedThreadPool(PERSISTENCE_POOL_SIZE);

        try {
            List<CompletableFuture<Void>> futures = entries.entrySet().stream()
                    .map(entry ->
                            CompletableFuture.runAsync(
                                    () -> historicalDataPersistence.persist(entry.getKey(), entry.getValue()), executor
                            ).exceptionally(ex -> {
                                logger.error("Failed to persist data for {}", entry.getKey(), ex);
                                return null;
                            })
                    )
                    .toList();

            // wait for all to complete
            CompletableFuture.allOf(futures.toArray(new CompletableFuture[0])).join();

        } finally {
            executor.shutdown();
        }
    }

}
