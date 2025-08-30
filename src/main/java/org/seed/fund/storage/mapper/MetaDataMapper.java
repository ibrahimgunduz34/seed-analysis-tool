package org.seed.fund.storage.mapper;

import org.seed.fund.model.MetaData;
import org.seed.fund.storage.jpa.entity.MetaDataEntity;
import org.springframework.stereotype.Component;

import java.util.Currency;

@Component
public class MetaDataMapper {
    public MetaDataEntity toEntity(MetaData model) {
        return new MetaDataEntity(
                model.getId(),
                model.getCode(),
                model.getName(),
                model.getFundType(),
                model.getCurrency().toString(),
                model.getCreatedAt()
        );
    }

    public MetaData toModel(MetaDataEntity entity) {
        return new MetaData(
                entity.getId(),
                entity.getCode(),
                entity.getName(),
                entity.getFundType(),
                Currency.getInstance(entity.getCurrency()),
                entity.getCreatedAt()
        );
    }
}
