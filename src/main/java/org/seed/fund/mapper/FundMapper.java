package org.seed.fund.mapper;

import org.seed.fund.model.Fund;
import org.seed.fund.storage.jpa.entity.HistoricalDataEntity;
import org.springframework.stereotype.Component;

@Component
public class FundMapper {
    private final MetaDataMapper metaDataMapper;
    private final HistoricalDataMapper historicalDataMapper;

    public FundMapper(MetaDataMapper metaDataMapper, HistoricalDataMapper historicalDataMapper) {
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
