package com.seed.fund.mapper;

import com.seed.fund.model.ExternalFundHistoricalData;
import com.seed.fund.model.Fund;
import com.seed.fund.model.FundMetaData;
import org.springframework.stereotype.Component;
import com.seed.fund.model.FundHistoricalData;
import com.seed.fund.storage.entity.FundHistoricalDataEntity;

import java.util.List;

@Component
public class HistoricalDataMapper {
    private final MetaDataMapper metaDataMapper;

    public HistoricalDataMapper(MetaDataMapper metaDataMapper) {
        this.metaDataMapper = metaDataMapper;
    }

    public List<FundHistoricalData> toModel(List<FundHistoricalDataEntity> entityList) {
        return entityList.stream().map(entity -> new FundHistoricalData(
                        entity.getValueDate(),
                        entity.getPrice(),
                        entity.getNumberOfShares(),
                        entity.getNumberOfInvestors(),
                        entity.getTotalValue()
                ))
                .toList();
    }

    public FundHistoricalData toModel(ExternalFundHistoricalData externalFundHistoricalData) {
        return new FundHistoricalData(
                externalFundHistoricalData.valueDate(),
                externalFundHistoricalData.price(),
                externalFundHistoricalData.numberOfShares(),
                externalFundHistoricalData.numberOfInvestors(),
                externalFundHistoricalData.totalValue()
        );
    }

    public FundHistoricalDataEntity toEntity(FundMetaData fundMetaData, FundHistoricalData fundHistoricalData) {
        return new FundHistoricalDataEntity(
                null,
                metaDataMapper.toEntity(fundMetaData),
                fundHistoricalData.numberOfShares(),
                fundHistoricalData.numberOfInvestors(),
                fundHistoricalData.totalValue(),
                fundHistoricalData.price(),
                fundHistoricalData.date()
        );
    }
}
