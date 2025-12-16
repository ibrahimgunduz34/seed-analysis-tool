package com.seed.fund.command;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Component;
import com.seed.core.DataProvider;
import com.seed.core.model.ServiceResponse;
import com.seed.core.storage.MetaDataStorage;
import com.seed.fund.mapper.MetaDataMapper;
import com.seed.fund.model.ExternalFundHistoricalData;
import com.seed.fund.model.ExternalFundMetaData;
import com.seed.fund.model.FundMetaData;

import java.util.List;

@Component
@ConditionalOnProperty(name = "task", havingValue = "MetaDataListSync")
public class MetaDataListSync implements CommandLineRunner {
    private final DataProvider<ExternalFundMetaData, ExternalFundHistoricalData> dataProvider;
    private final MetaDataStorage<FundMetaData> metaDataStorage;
    private final MetaDataMapper metaDataMapper;

    private static final Logger logger = LoggerFactory.getLogger(MetaDataListSync.class);

    public MetaDataListSync(DataProvider<ExternalFundMetaData, ExternalFundHistoricalData> dataProvider,
                            MetaDataStorage<FundMetaData> metaDataStorage,
                            MetaDataMapper metaDataMapper) {
        this.dataProvider = dataProvider;
        this.metaDataStorage = metaDataStorage;
        this.metaDataMapper = metaDataMapper;
    }

    @Override
    public void run(String... args) throws Exception {
        logger.info("Fetching meta data from the provider");

        ServiceResponse<List<ExternalFundMetaData>> exported = dataProvider.exportMetaData();

        if (exported.getError().isPresent()) {
            throw new RuntimeException("Failed to fetch provider data");
        }

        List<ExternalFundMetaData> exportedMetaDataList = exported.getData().orElseThrow(() -> new RuntimeException("Failed to fetch provider data"));

        List<String> existingMetaDataCodes = metaDataStorage.getAllMetaData()
                .stream().map(FundMetaData::code)
                .toList();

        List<FundMetaData> cleanedMetaData = exportedMetaDataList.stream()
                .filter(item -> !existingMetaDataCodes.contains(item.code()))
                .map(metaDataMapper::toModel)
                .toList();


        logger.info("Saving meta data");
        metaDataStorage.saveMetaData(cleanedMetaData);

        logger.info("{} rows created. Meta data synchronization process has been completed.", cleanedMetaData.size());

        System.exit(0);
    }
}
