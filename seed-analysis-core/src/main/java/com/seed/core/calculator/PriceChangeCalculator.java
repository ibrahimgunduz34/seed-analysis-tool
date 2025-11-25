package com.seed.core.calculator;

import com.seed.core.CalculationContext;
import com.seed.core.HistoricalData;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.List;

@Component
public class PriceChangeCalculator implements Calculator {
    @Override
    public void calculate(CalculationContext ctx) {
        List<HistoricalData> historicalDataList = ctx.getAsset().getHistoricalData();

        BigDecimal startPrice = historicalDataList.stream().findFirst().map(HistoricalData::getPrice).orElse(BigDecimal.ZERO);

        BigDecimal endPrice = historicalDataList.isEmpty()
                ? BigDecimal.ZERO
                : historicalDataList.getLast().getPrice();

        BigDecimal change = BigDecimal.ZERO;

        if (startPrice.compareTo(BigDecimal.ZERO) != 0) {
            change = endPrice.subtract(startPrice)
                    .divide(startPrice, 10, RoundingMode.HALF_UP);
        }

        ctx.setPriceChange(change);
    }
}
