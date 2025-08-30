package org.seed.fund.storage.mapper;

import org.seed.fund.storage.jpa.entity.HistoricalDataEntity;
import org.seed.fund.model.HistoricalData;
import org.seed.fund.model.MetaData;
import org.springframework.stereotype.Component;

@Component
public class StorageHistoricalDataMapper {
    private final StorageMetaDataMapper metaDataMapper;;

    public StorageHistoricalDataMapper(StorageMetaDataMapper metaDataMapper) {
        this.metaDataMapper = metaDataMapper;
    }

    public HistoricalData toModel(HistoricalDataEntity historicalDataEntity) {
        return new HistoricalData(
                historicalDataEntity.getId(),
                historicalDataEntity.getNumberOfShares(),
                historicalDataEntity.getNumberOfInvestors(),
                historicalDataEntity.getTotalValue(),
                historicalDataEntity.getPrice(),
                historicalDataEntity.getValueDate(),
                historicalDataEntity.getCreatedAt()
        );
    }

    public HistoricalDataEntity toEntity(MetaData metaData, HistoricalData historicalData) {
        return new HistoricalDataEntity(
                historicalData.getId(),
                metaDataMapper.toEntity(metaData),
                historicalData.getNumberOfShares(),
                historicalData.getNumberOfInvestors(),
                historicalData.getTotalValue(),
                historicalData.getPrice(),
                historicalData.getValueDate(),
                historicalData.getCreatedAt()
        );
    }
}
