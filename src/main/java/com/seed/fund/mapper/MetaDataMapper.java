package com.seed.fund.mapper;

import com.seed.fund.model.ExternalFundMetaData;
import com.seed.fund.model.FundMetaData;
import com.seed.fund.storage.entity.FundMetaDataEntity;
import org.springframework.stereotype.Component;

import java.util.Currency;

@Component
public class MetaDataMapper {
    public FundMetaData toModel(FundMetaDataEntity entity) {
        return new FundMetaData(
                entity.getId(),
                entity.getCode(),
                entity.getName(),
                entity.getFundType(),
                Currency.getInstance(entity.getCurrency())
        );
    }

    public FundMetaData toModel(ExternalFundMetaData externalFundMetaData) {
        return new FundMetaData(
                null,
                externalFundMetaData.code(),
                externalFundMetaData.name(),
                externalFundMetaData.fundType(),
                externalFundMetaData.currency()
        );
    }

    public FundMetaDataEntity toEntity(FundMetaData model) {
        return new FundMetaDataEntity(
                model.id(),
                model.code(),
                model.name(),
                model.fundType(),
                model.currency().toString()
        );
    }
}
