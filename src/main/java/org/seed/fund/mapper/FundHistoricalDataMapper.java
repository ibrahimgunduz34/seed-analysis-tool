package org.seed.fund.mapper;

import org.seed.fund.model.ExternalFundHistoricalData;
import org.seed.fund.model.FundHistoricalData;
import org.seed.fund.model.FundMetaData;
import org.seed.fund.storage.jpa.entity.FundHistoricalDataEntity;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class FundHistoricalDataMapper {
    private final FundMetaDataMapper fundMetaDataMapper;;

    public FundHistoricalDataMapper(FundMetaDataMapper fundMetaDataMapper) {
        this.fundMetaDataMapper = fundMetaDataMapper;
    }

    public FundHistoricalData toModel(FundHistoricalDataEntity fundHistoricalDataEntity) {
        return new FundHistoricalData(
                fundHistoricalDataEntity.getId(),
                fundHistoricalDataEntity.getNumberOfShares(),
                fundHistoricalDataEntity.getNumberOfInvestors(),
                fundHistoricalDataEntity.getTotalValue(),
                fundHistoricalDataEntity.getPrice(),
                fundHistoricalDataEntity.getValueDate(),
                fundHistoricalDataEntity.getCreatedAt()
        );
    }

    public List<FundHistoricalData> toModel(List<FundHistoricalDataEntity> entities) {
        return entities.stream().map(this::toModel).toList();
    }

    public FundHistoricalData toModel(ExternalFundHistoricalData externalFundHistoricalData) {
        return FundHistoricalData.create(
                externalFundHistoricalData.getNumberOfShares(),
                externalFundHistoricalData.getNumberOfInvestors(),
                externalFundHistoricalData.getTotalValue(),
                externalFundHistoricalData.getPrice(),
                externalFundHistoricalData.getValueDate()
        );
    }

    public FundHistoricalDataEntity toEntity(FundMetaData fundMetaData, FundHistoricalData fundHistoricalData) {
        return new FundHistoricalDataEntity(
                fundHistoricalData.getId(),
                fundMetaDataMapper.toEntity(fundMetaData),
                fundHistoricalData.getNumberOfShares(),
                fundHistoricalData.getNumberOfInvestors(),
                fundHistoricalData.getTotalValue(),
                fundHistoricalData.getPrice(),
                fundHistoricalData.getValueDate(),
                fundHistoricalData.getCreatedAt()
        );
    }
}
