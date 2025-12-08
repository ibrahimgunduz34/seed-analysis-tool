package com.seed.core.calculator;

import com.seed.core.AnalysisContext;
import com.seed.core.model.Candle;
import com.seed.core.model.ResultKey;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.List;
import java.util.Map;

public class Mdd<C extends Candle> implements Calculator<C> {
    public static final ResultKey<BigDecimal> MDD = ResultKey.of("MDD", BigDecimal.class);

    @Override
    public List<ResultKey<?>> requires() {
        return List.of();
    }

    @Override
    public List<ResultKey<?>> produces() {
        return List.of();
    }

    @Override
    public Map<ResultKey<?>, Object> calculate(AnalysisContext<?, C> ctx) {
        List<C> candles = ctx.getHistoricalData().candles();
        if (candles.isEmpty()) {
            return Map.of();
        }

        BigDecimal peak = candles.stream()
                .findFirst().map(Candle::price).orElse(BigDecimal.ZERO);

        BigDecimal mdd = BigDecimal.ZERO;

        for (Candle candle : candles) {
            BigDecimal price = candle.price();
            if (price.compareTo(peak) > 0) {
                peak = price;
            }

            if (peak.compareTo(BigDecimal.ZERO) == 0) {
                continue;
            }

            BigDecimal dd = price.divide(peak, 10, RoundingMode.HALF_UP).subtract(BigDecimal.ONE);

            if (dd.compareTo(mdd) < 0) {
                mdd = dd;
            }
        }

        return Map.of(
                MDD, mdd
        );
    }
}
