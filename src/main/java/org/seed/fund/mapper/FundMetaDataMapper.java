package org.seed.fund.mapper;

import org.seed.fund.model.ExternalFundMetaData;
import org.seed.fund.model.FundMetaData;
import org.seed.fund.storage.jpa.entity.FundMetaDataEntity;
import org.springframework.stereotype.Component;

import java.util.Currency;

@Component
public class FundMetaDataMapper {
    public FundMetaDataEntity toEntity(FundMetaData model) {
        return new FundMetaDataEntity(
                model.getId(),
                model.getCode(),
                model.getName(),
                model.getFundType(),
                model.getCurrency().toString(),
                model.getCreatedAt()
        );
    }

    public FundMetaData toModel(FundMetaDataEntity entity) {
        return new FundMetaData(
                entity.getId(),
                entity.getCode(),
                entity.getName(),
                entity.getFundType(),
                Currency.getInstance(entity.getCurrency()),
                entity.getCreatedAt()
        );
    }

    public FundMetaData toModel(ExternalFundMetaData externalFundMetaData) {
        return FundMetaData.create(
                externalFundMetaData.getCode(),
                externalFundMetaData.getName(),
                externalFundMetaData.getFundType(),
                externalFundMetaData.getCurrency()
        );
    }
}
