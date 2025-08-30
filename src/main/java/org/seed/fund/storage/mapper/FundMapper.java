package org.seed.fund.storage.mapper;

import org.seed.fund.model.Fund;
import org.seed.fund.storage.jpa.entity.HistoricalDataEntity;
import org.springframework.stereotype.Component;

@Component
public class FundMapper {
    private final StorageMetaDataMapper metaDataMapper;
    private final StorageHistoricalDataMapper historicalDataMapper;

    public FundMapper(StorageMetaDataMapper metaDataMapper, StorageHistoricalDataMapper historicalDataMapper) {
        this.metaDataMapper = metaDataMapper;
        this.historicalDataMapper = historicalDataMapper;
    }

    public Fund toModel(HistoricalDataEntity historicalDataEntity) {
        return new Fund(
                historicalDataEntity.getMetaData().getId(),
                metaDataMapper.toModel(historicalDataEntity.getMetaData()),
                historicalDataMapper.toModel(historicalDataEntity)
        );
    }
}
