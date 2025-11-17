package org.seed.fund.command;


import org.seed.common.HistoricalDataSynchronizer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

@Component
@ConditionalOnProperty(name = "task", havingValue = "HistoricalDataListSyncAll")
public class HistoricalDataListSyncAll implements CommandLineRunner {
    private final Logger logger = LoggerFactory.getLogger(HistoricalDataListSyncAll.class);

    private final HistoricalDataSynchronizer synchronizer;

    public HistoricalDataListSyncAll(HistoricalDataSynchronizer synchronizer) {
        this.synchronizer = synchronizer;
    }

    @Override
    public void run(String... args) throws Exception {
        logger.info("Starting HistoricalDataListSyncAll job");

//        LocalDate valueDate = LocalDate.now();
        LocalDate beginDate = LocalDate.parse(args[0], DateTimeFormatter.ofPattern("yyyy-MM-dd"));
        LocalDate endDate = LocalDate.parse(args[1], DateTimeFormatter.ofPattern("yyyy-MM-dd"));

//        LocalDate startDate = LocalDate.parse("2020-08-29", DateTimeFormatter.ofPattern("yyyy-MM-dd"));
//        LocalDate endDate = LocalDate.parse("2021-08-29", DateTimeFormatter.ofPattern("yyyy-MM-dd"));
        synchronizer.synchronize(beginDate, endDate);

        logger.info("HistoricalDataListSyncAll Job has been completed");
        System.exit(0);
    }
}
