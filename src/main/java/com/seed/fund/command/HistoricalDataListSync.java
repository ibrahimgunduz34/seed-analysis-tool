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
@ConditionalOnProperty(name = "task", havingValue = "HistoricalDataListSync")
public class HistoricalDataListSync implements CommandLineRunner {
    private final Logger logger = LoggerFactory.getLogger(HistoricalDataListSync.class);
    private final HistoricalDataCollector historicalDataCollector;
    private final HistoricalDataPersistence historicalDataPersistence;

    public HistoricalDataListSync(HistoricalDataCollector historicalDataCollector,
                                  HistoricalDataPersistence historicalDataPersistence) {
        this.historicalDataCollector = historicalDataCollector;
        this.historicalDataPersistence = historicalDataPersistence;
    }

    @Override
    public void run(String... args) throws Exception {
        LocalDate currentDate = LocalDate.now();

        List<ExternalFundHistoricalData> externalHistoricalDataList = historicalDataCollector.collect(currentDate, currentDate);

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
