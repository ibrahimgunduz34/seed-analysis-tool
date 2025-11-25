package com.seed.core.calculator;

import com.seed.core.CalculationContext;
import com.seed.core.HistoricalData;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.List;
import java.util.function.IntFunction;
import java.util.stream.IntStream;

@Component
public class DailyChangeCalculator implements Calculator {
    @Override
    public void calculate(CalculationContext ctx) {
        List<HistoricalData> historicalDataList = ctx.getAsset().getHistoricalData();
        int numberOfRows = historicalDataList.size();
        List<BigDecimal> changes = IntStream.range(1, numberOfRows)
                .mapToObj(getBigDecimalIntFunction(historicalDataList))
                .toList();

        ctx.setDailyChanges(changes);
        ctx.setNumberOfDays(numberOfRows);
    }

    private static IntFunction<BigDecimal> getBigDecimalIntFunction(List<HistoricalData> historicalDataList) {
        return i -> {
            BigDecimal prev = historicalDataList.get(i - 1).getPrice();
            BigDecimal current = historicalDataList.get(i).getPrice();
            if (prev.compareTo(BigDecimal.ZERO) == 0) {
                return BigDecimal.ZERO;
            }
            return current.subtract(prev).divide(
                    prev,
                    10,
                    RoundingMode.HALF_UP
            );
        };
    }
}
