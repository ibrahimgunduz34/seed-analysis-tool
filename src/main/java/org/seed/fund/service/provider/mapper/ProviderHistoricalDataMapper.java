package org.seed.fund.service.provider.mapper;

import org.seed.fund.model.ExternalHistoricalData;
import org.seed.fund.model.HistoricalData;
import org.springframework.stereotype.Component;

@Component
public class ProviderHistoricalDataMapper {
    public HistoricalData toModel(ExternalHistoricalData externalHistoricalData) {
        return HistoricalData.create(
                externalHistoricalData.getNumberOfShares(),
                externalHistoricalData.getNumberOfInvestors(),
                externalHistoricalData.getTotalValue(),
                externalHistoricalData.getPrice(),
                externalHistoricalData.getValueDate()
        );
    }
}
