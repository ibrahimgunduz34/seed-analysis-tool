package org.seed.fund.mapper;

import org.seed.fund.model.Fund;
import org.seed.fund.storage.jpa.entity.FundHistoricalDataEntity;
import org.springframework.stereotype.Component;

@Component
public class FundMapper {
    private final FundMetaDataMapper fundMetaDataMapper;
    private final FundHistoricalDataMapper fundHistoricalDataMapper;

    public FundMapper(FundMetaDataMapper fundMetaDataMapper, FundHistoricalDataMapper fundHistoricalDataMapper) {
        this.fundMetaDataMapper = fundMetaDataMapper;
        this.fundHistoricalDataMapper = fundHistoricalDataMapper;
    }

    public Fund toModel(FundHistoricalDataEntity fundHistoricalDataEntity) {
        return new Fund(
                fundHistoricalDataEntity.getMetaData().getId(),
                fundMetaDataMapper.toModel(fundHistoricalDataEntity.getMetaData()),
                fundHistoricalDataMapper.toModel(fundHistoricalDataEntity)
        );
    }
}
