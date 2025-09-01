package org.seed.fund.mapper;

import org.seed.fund.model.ExternalHistoricalData;
import org.seed.fund.model.HistoricalData;
import org.seed.fund.model.MetaData;
import org.seed.fund.storage.jpa.entity.HistoricalDataEntity;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class HistoricalDataMapper {
    private final MetaDataMapper metaDataMapper;;

    public HistoricalDataMapper(MetaDataMapper metaDataMapper) {
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

    public List<HistoricalData> toModel(List<HistoricalDataEntity> entities) {
        return entities.stream().map(this::toModel).toList();
    }

    public HistoricalData toModel(ExternalHistoricalData externalHistoricalData) {
        return HistoricalData.create(
                externalHistoricalData.getNumberOfShares(),
                externalHistoricalData.getNumberOfInvestors(),
                externalHistoricalData.getTotalValue(),
                externalHistoricalData.getPrice(),
                externalHistoricalData.getValueDate()
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
