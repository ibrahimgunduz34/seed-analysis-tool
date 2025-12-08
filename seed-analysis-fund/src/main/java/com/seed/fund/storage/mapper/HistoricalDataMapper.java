package com.seed.fund.storage.mapper;

import com.seed.fund.model.FundHistoricalData;
import com.seed.fund.storage.entity.FundHistoricalDataEntity;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class HistoricalDataMapper {
    public List<FundHistoricalData> toModel(List<FundHistoricalDataEntity> entityList) {
        return entityList.stream().map(entity -> new FundHistoricalData(
                        entity.getValueDate(),
                        entity.getPrice()
                ))
                .toList();
    }
}
