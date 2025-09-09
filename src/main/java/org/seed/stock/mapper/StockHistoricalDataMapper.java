package org.seed.stock.mapper;

import org.seed.stock.model.ExternalStockHistoricalData;
import org.seed.stock.model.StockHistoricalData;
import org.seed.stock.model.StockMetaData;
import org.seed.stock.storage.jpa.entity.StockHistoricalDataEntity;

import java.time.LocalDateTime;

public class StockHistoricalDataMapper {
    public StockHistoricalDataEntity toEntity(StockMetaData metaData, StockHistoricalData historicalData) {
        return null;
    }

    public StockHistoricalData toModel(StockHistoricalDataEntity entity) {
        return null;
    }

    public StockHistoricalData toModel(ExternalStockHistoricalData externalStockHistoricalData) {
        return null;
    }
}
