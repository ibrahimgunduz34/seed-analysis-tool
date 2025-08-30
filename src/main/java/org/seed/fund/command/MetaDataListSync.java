package org.seed.fund.command;

import org.seed.fund.service.MetaDataSynchronizer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Component;

@Component
@ConditionalOnProperty(name = "task", havingValue = "MetaDataListSync")
public class MetaDataListSync implements CommandLineRunner {
    private final Logger logger = LoggerFactory.getLogger(MetaDataListSync.class);
    private final MetaDataSynchronizer metaDataSynchronizer;

    public MetaDataListSync(MetaDataSynchronizer metaDataSynchronizer) {
        this.metaDataSynchronizer = metaDataSynchronizer;
    }

    @Override
    public void run(String... args) throws Exception {
        logger.info("Starting MetaDataListSync job");

        metaDataSynchronizer.synchronize();

        logger.info("MetaDataListSync Job has been completed");
        System.exit(0);
    }
}
