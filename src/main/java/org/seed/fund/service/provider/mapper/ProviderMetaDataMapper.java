package org.seed.fund.service.provider.mapper;

import org.seed.fund.model.ExternalMetaData;
import org.seed.fund.model.MetaData;
import org.springframework.stereotype.Component;

@Component
public class ProviderMetaDataMapper {
    public MetaData toModel(ExternalMetaData externalMetaData) {
        return MetaData.create(
                externalMetaData.getCode(),
                externalMetaData.getName(),
                externalMetaData.getFundType(),
                externalMetaData.getCurrency()
        );
    }
}
