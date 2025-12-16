package com.seed.fund.command;

import com.seed.fund.HistoricalDataCollector;
import com.seed.fund.HistoricalDataPersistence;
import com.seed.fund.model.ExternalFundHistoricalData;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;

@Component
@ConditionalOnProperty(name = "task", havingValue = "HistoricalDataListSyncAll")
public class HistoricalDataListSyncAll implements CommandLineRunner {
    private final Logger logger = LoggerFactory.getLogger(HistoricalDataListSyncAll.class);
    private final HistoricalDataCollector historicalDataCollector;
    private final HistoricalDataPersistence historicalDataPersistence;

    public HistoricalDataListSyncAll(HistoricalDataCollector historicalDataCollector,
                                     HistoricalDataPersistence historicalDataPersistence) {
        this.historicalDataCollector = historicalDataCollector;
        this.historicalDataPersistence = historicalDataPersistence;
    }

    @Override
    public void run(String... args) throws Exception {
        LocalDate startDate = LocalDate.parse(args[0], DateTimeFormatter.ofPattern("yyyy-MM-dd"));
        LocalDate endDate = LocalDate.parse(args[1], DateTimeFormatter.ofPattern("yyyy-MM-dd"));

        List<ExternalFundHistoricalData> externalHistoricalDataList = historicalDataCollector.collect(startDate, endDate);

        if (externalHistoricalDataList.isEmpty()) {
            logger.info("No historical data received");
            System.exit(0);
            return;
        }

        historicalDataPersistence.persist(externalHistoricalDataList);

        logger.info("Historical data synchronization is completed.");

        System.exit(0);
    }
}
