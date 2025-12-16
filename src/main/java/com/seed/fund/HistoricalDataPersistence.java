package com.seed.fund;

import com.seed.core.storage.HistoricalDataStorage;
import com.seed.core.storage.MetaDataStorage;
import com.seed.fund.mapper.HistoricalDataMapper;
import com.seed.fund.model.ExternalFundHistoricalData;
import com.seed.fund.model.Fund;
import com.seed.fund.model.FundHistoricalData;
import com.seed.fund.model.FundMetaData;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.function.Function;
import java.util.stream.Collectors;

@Component
public class HistoricalDataPersistence {
    private final Logger logger = LoggerFactory.getLogger(HistoricalDataPersistence.class);
    private static final Integer PERSISTENCE_POOL_SIZE = 4;

    private final MetaDataStorage<FundMetaData> metaDataStorage;
    private final HistoricalDataStorage<FundMetaData, FundHistoricalData> historicalDataStorage;
    private final HistoricalDataMapper historicalDataMapper;

    public HistoricalDataPersistence(MetaDataStorage<FundMetaData> metaDataStorage,
                                     HistoricalDataStorage<FundMetaData, FundHistoricalData> historicalDataStorage, HistoricalDataMapper historicalDataMapper) {
        this.metaDataStorage = metaDataStorage;
        this.historicalDataStorage = historicalDataStorage;
        this.historicalDataMapper = historicalDataMapper;
    }

    public void persist(List<ExternalFundHistoricalData> historicalData) {
        Map<String, FundMetaData> metaDataMap = metaDataStorage.getAllMetaData().stream()
                .collect(Collectors.toMap(FundMetaData::code, Function.identity()));

        Set<Map.Entry<LocalDate, List<ExternalFundHistoricalData>>> groupedHistoricalData = historicalData
                .stream()
                .collect(Collectors.groupingBy(ExternalFundHistoricalData::valueDate))
                .entrySet();

        ExecutorService executor = Executors.newFixedThreadPool(PERSISTENCE_POOL_SIZE);
        ArrayList< CompletableFuture<Void>> futures = new ArrayList<>();;

        try {
            for (var entry : groupedHistoricalData) {
                CompletableFuture<Void> future = CompletableFuture.runAsync(() -> {
                    try {
                        Thread.sleep(100);
                    } catch (InterruptedException e) {
                        Thread.currentThread().interrupt();
                    }
                    persistBatch(entry.getKey(), entry.getValue(), metaDataMap);
                }, executor);

                futures.add(future);
            }

            List<Void> list = futures.stream()
                    .map(CompletableFuture::join)
                    .toList();
        } finally {
            executor.shutdown();
        }
    }

    private void persistBatch(LocalDate currentValueDate, List<ExternalFundHistoricalData> historicalDataList, Map<String, FundMetaData> metaDataMap) {
        List<String> valueDateCodes = metaDataStorage.getAllMetaDataByValueDate(currentValueDate)
                .stream().map(FundMetaData::code)
                .toList();

        List<Fund> funds = historicalDataList.stream()
                .filter(item -> !valueDateCodes.contains(item.code()) && metaDataMap.containsKey(item.code()))
                .map(item -> new Fund(metaDataMap.get(item.code()), List.of(historicalDataMapper.toModel(item))))
                .toList();

        historicalDataStorage.saveAll(funds);

        logger.info("Stored {} row(s)", funds.size());
    }
}
