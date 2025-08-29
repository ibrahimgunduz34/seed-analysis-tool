package org.seed.command;

import org.seed.fund.FundStorage;
import org.seed.fund.MetaDataService;
import org.seed.fund.model.ExternalMetaData;
import org.seed.fund.model.MetaData;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@ConditionalOnProperty(name = "task", havingValue = "MetaDataListSync")
public class MetaDataListSync implements CommandLineRunner {
    private final Logger logger = LoggerFactory.getLogger(MetaDataListSync.class);

    private final MetaDataService metaDataService;
    private final FundStorage fundStorage;

    public MetaDataListSync(MetaDataService metaDataService, FundStorage fundStorage) {
        this.metaDataService = metaDataService;
        this.fundStorage = fundStorage;
    }

    @Override
    public void run(String... args) throws Exception {
        logger.info("Starting MetaDataListSync job");

        List<ExternalMetaData> externalMetaDataList = metaDataService.retrieveList()
                .getData()
                .orElseThrow(() -> new RuntimeException("Failed to retrieve metadata list"));

        List<String> existingMetaDataCodes = fundStorage.getMetaDataList().stream().map(MetaData::getCode).toList();

        List<MetaData> metaDataList = externalMetaDataList.stream()
                .filter(item -> !existingMetaDataCodes.contains(item.getCode()))
                .map(item -> MetaData.create(item.getCode(), item.getName(), item.getFundType(), item.getCurrency()))
                .toList();

        if (metaDataList.isEmpty()) {
            logger.info("MetaDataListSync Job has been completed without any actions");
            System.exit(0);
            return;
        }

        fundStorage.saveAll(metaDataList);

        logger.info("Created " + metaDataList.size() + " item(s).");

        logger.info("MetaDataListSync Job has been completed");
        System.exit(0);
    }
}
