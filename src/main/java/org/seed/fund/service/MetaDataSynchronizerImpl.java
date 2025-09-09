package org.seed.fund.service;

import org.seed.fund.mapper.FundMetaDataMapper;
import org.seed.fund.model.ExternalFundMetaData;
import org.seed.fund.model.FundMetaData;
import org.seed.fund.service.provider.MetaDataService;
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
    private final FundMetaDataMapper fundMetaDataMapper;

    public MetaDataSynchronizerImpl(MetaDataService metaDataService, FundStorage fundStorage, FundMetaDataMapper fundMetaDataMapper) {
        this.metaDataService = metaDataService;
        this.fundStorage = fundStorage;
        this.fundMetaDataMapper = fundMetaDataMapper;
    }

    @Override
    public void synchronize() {
        List<ExternalFundMetaData> externalFundMetaDataList = metaDataService.retrieveList()
                .getData()
                .orElseThrow(() -> new RuntimeException("Failed to retrieve metadata list"));

        List<String> existingMetaDataCodes = fundStorage.getMetaDataList().stream().map(FundMetaData::getCode).toList();

        List<FundMetaData> fundMetaDataList = externalFundMetaDataList.stream()
                .filter(item -> !existingMetaDataCodes.contains(item.getCode()))
                .map(fundMetaDataMapper::toModel)
                .toList();

        if (fundMetaDataList.isEmpty()) {
            logger.info("Meta data is already in sync");
            return;
        }

        fundStorage.saveAll(fundMetaDataList);

        logger.info("Created " + fundMetaDataList.size() + " item(s).");
    }
}
