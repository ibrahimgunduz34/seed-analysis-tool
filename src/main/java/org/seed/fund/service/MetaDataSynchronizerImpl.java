package org.seed.fund.service;

import org.seed.fund.command.MetaDataListSync;
import org.seed.fund.model.ExternalMetaData;
import org.seed.fund.model.MetaData;
import org.seed.fund.service.provider.MetaDataService;
import org.seed.fund.service.provider.mapper.ProviderMetaDataMapper;
import org.seed.fund.storage.FundStorage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class MetaDataSynchronizerImpl implements MetaDataSynchronizer {
    private final Logger logger = LoggerFactory.getLogger(MetaDataSynchronizerImpl.class);

    private final MetaDataService metaDataService;
    private final FundStorage fundStorage;
    private final ProviderMetaDataMapper metaDataMapper;

    public MetaDataSynchronizerImpl(MetaDataService metaDataService, FundStorage fundStorage, ProviderMetaDataMapper metaDataMapper) {
        this.metaDataService = metaDataService;
        this.fundStorage = fundStorage;
        this.metaDataMapper = metaDataMapper;
    }

    @Override
    public void synchronize() {
        List<ExternalMetaData> externalMetaDataList = metaDataService.retrieveList()
                .getData()
                .orElseThrow(() -> new RuntimeException("Failed to retrieve metadata list"));

        List<String> existingMetaDataCodes = fundStorage.getMetaDataList().stream().map(MetaData::getCode).toList();

        List<MetaData> metaDataList = externalMetaDataList.stream()
                .filter(item -> !existingMetaDataCodes.contains(item.getCode()))
                .map(metaDataMapper::toModel)
                .toList();

        if (metaDataList.isEmpty()) {
            logger.info("Meta data is already in sync");
            return;
        }

        fundStorage.saveAll(metaDataList);

        logger.info("Created " + metaDataList.size() + " item(s).");
    }
}
