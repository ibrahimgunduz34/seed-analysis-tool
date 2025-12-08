package com.seed.fund.storage.mapper;

import com.seed.core.model.HistoricalData;
import com.seed.fund.model.FundCandle;
import com.seed.fund.storage.entity.FundHistoricalDataEntity;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;

@Component
public class HistoricalDataMapper {
    public HistoricalData<FundCandle> toModel(List<FundHistoricalDataEntity> entityList) {
        List<FundCandle> candleList = entityList.stream().map(entity -> new FundCandle(
                        entity.getValueDate(),
                        entity.getPrice()
                ))
                .toList();

        if (candleList.isEmpty()) {
            return new HistoricalData<>(List.of());
        }

        return new HistoricalData<>(candleList);
    }
}
