package com.seed.fund;

import com.seed.core.DataProvider;
import com.seed.core.exception.ExternalServiceConnectionFailure;
import com.seed.fund.model.ExternalFundHistoricalData;
import com.seed.fund.model.ExternalFundMetaData;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.CompletionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.function.BiFunction;

@Component
public class HistoricalDataCollector {
    private final Logger logger = LoggerFactory.getLogger(HistoricalDataCollector.class);
    private static final Integer FETCH_POOL_SIZE = 8;

    private final DataProvider<ExternalFundMetaData, ExternalFundHistoricalData> dataProvider;

    public HistoricalDataCollector(DataProvider<ExternalFundMetaData, ExternalFundHistoricalData> dataProvider) {
        this.dataProvider = dataProvider;
    }

    public List<ExternalFundHistoricalData> collect(LocalDate startDate, LocalDate endDate) {
        ExecutorService executor = Executors.newFixedThreadPool(FETCH_POOL_SIZE);
        List<CompletableFuture<List<ExternalFundHistoricalData>>> futures = new ArrayList<>();

        try {
            for (LocalDate valueDate = startDate; !valueDate.isAfter(endDate); valueDate = valueDate.plusDays(1)) {
                final LocalDate currentValueDate = valueDate;

                CompletableFuture<List<ExternalFundHistoricalData>> future = CompletableFuture.supplyAsync(() -> {
                            try {
                                Thread.sleep(800); // 500ms delay before calling fetchData
                            } catch (InterruptedException e) {
                                Thread.currentThread().interrupt();
                            }
                            return fetchHistoricalData(currentValueDate);
                        }, executor)
                        .handle(handleBatchProviderResponse(currentValueDate));

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

    private List<ExternalFundHistoricalData> fetchHistoricalData(LocalDate valueDate) {
        return dataProvider.exportHistoricalData(valueDate)
                .getData().orElseThrow(() -> new ExternalServiceConnectionFailure("Failed to fetch historical data for: " + valueDate));
    }

    private BiFunction<List<ExternalFundHistoricalData>, Throwable, List<ExternalFundHistoricalData>> handleBatchProviderResponse(LocalDate requestDate) {
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
                return List.<ExternalFundHistoricalData>of();
            } else {
                logger.info("Collected {} item(s) for {}", items.size(), requestDate);
                return items;
            }
        };
    }
}
