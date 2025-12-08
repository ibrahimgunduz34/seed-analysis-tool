package com.seed.core.calculator;

import com.seed.core.AnalysisContext;
import com.seed.core.model.Candle;
import com.seed.core.model.ResultKey;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.List;
import java.util.Map;

public class PeriodPriceChange<C extends Candle> implements Calculator<C> {
    public static final ResultKey<BigDecimal> PERIOD_PRICE_CHANGE = ResultKey.of("Period Price change", BigDecimal.class);

    @Override
    public List<ResultKey<?>> requires() {
        return List.of();
    }

    @Override
    public List<ResultKey<?>> produces() {
        return List.of(
                PERIOD_PRICE_CHANGE
        );
    }

    @Override
    public Map<ResultKey<?>, Object> calculate(AnalysisContext<?, C> ctx) {
        List<C> candles = ctx.getHistoricalData().candles();
        if (candles.isEmpty()) {
            return Map.of();
        }

        BigDecimal startPrice = candles.stream()
                .findFirst().map(Candle::price).orElse(BigDecimal.ZERO);

        BigDecimal endPrice = candles.get(candles.size() - 1).price();

        if (startPrice.compareTo(BigDecimal.ZERO) == 0) {
            return Map.of();
        }

        BigDecimal change = endPrice.subtract(startPrice)
                .divide(startPrice, 10, RoundingMode.HALF_UP);

        return Map.of(
                PeriodPriceChange.PERIOD_PRICE_CHANGE, change
        );
    }
}
