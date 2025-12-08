package com.seed.fund.storage.mapper;

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
}
