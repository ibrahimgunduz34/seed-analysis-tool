package org.seed.fund.report.mapper;

import org.seed.fund.model.HistoricalData;
import org.seed.fund.report.model.HistoricalPrice;

import java.math.BigDecimal;
import java.util.function.Function;

public class HistoricalPriceMapper implements Function<HistoricalData, HistoricalPrice> {
    private HistoricalData prev;

    @Override
    public HistoricalPrice apply(HistoricalData historicalData) {
        BigDecimal change = prev == null ? BigDecimal.ZERO
                : historicalData.getPrice().subtract(prev.getPrice());

        return new HistoricalPrice(historicalData.getValueDate(), historicalData.getPrice(), change);
    }
}
