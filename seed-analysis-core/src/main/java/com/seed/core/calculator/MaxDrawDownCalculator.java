package com.seed.core.calculator;

import com.seed.core.CalculationContext;
import com.seed.core.HistoricalData;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.List;

@Component
public class MaxDrawDownCalculator implements Calculator {

    @Override
    public void calculate(CalculationContext ctx) {
        if (ctx.getNumberOfDays() == 0 || ctx.getMean().compareTo(BigDecimal.ZERO) == 0) {
            ctx.setMaxDrawDown(BigDecimal.ZERO);
            return;
        }

        List<HistoricalData> historicalDataList = ctx.getAsset().getHistoricalData();

        BigDecimal peak = historicalDataList.getFirst().getPrice();
        BigDecimal maxDrawdown = BigDecimal.ZERO;

        for (HistoricalData historicalData : historicalDataList) {
            BigDecimal price = historicalData.getPrice();
            if (price.compareTo(peak) > 0) {
                peak = price;
            }

            if (peak.compareTo(BigDecimal.ZERO) == 0) {
                continue; // Peak sıfırsa drawdown hesaplanamaz, atla
            }

            // Drawdown = (CurrentPrice / Peak) - 1
            BigDecimal drawdown = price.divide(peak, 10, RoundingMode.HALF_UP).subtract(BigDecimal.ONE);

            if (drawdown.compareTo(maxDrawdown) < 0) {
                maxDrawdown = drawdown;
            }
        }

        ctx.setMaxDrawDown(maxDrawdown); // Ham veri olarak saklanıyor, raporda *100 ile gösterilecek
    }
}
