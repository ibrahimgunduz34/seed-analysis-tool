package org.seed.fund.command;


import org.seed.common.HistoricalDataSynchronizer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Component;

import java.time.LocalDate;

@Component
@ConditionalOnProperty(name = "task", havingValue = "HistoricalDataListSync")
public class HistoricalDataListSync implements CommandLineRunner {
    private final Logger logger = LoggerFactory.getLogger(HistoricalDataListSync.class);

    private final HistoricalDataSynchronizer synchronizer;

    public HistoricalDataListSync(HistoricalDataSynchronizer synchronizer) {
        this.synchronizer = synchronizer;
    }

    @Override
    public void run(String... args) throws Exception {
        logger.info("Starting HistoricalDataListSync job");

        LocalDate valueDate = LocalDate.now();

        synchronizer.synchronize(valueDate);

        logger.info("HistoricalDataListSync Job has been completed");
        System.exit(0);
    }
}
