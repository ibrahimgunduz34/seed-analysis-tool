package com.seed.core.calculator;

import com.seed.core.AnalysisContext;
import com.seed.core.model.HistoricalData;
import com.seed.core.model.ResultKey;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.List;
import java.util.Map;

public class Mdd<H extends HistoricalData> implements Calculator<H> {
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
    public Map<ResultKey<?>, Object> calculate(AnalysisContext<?, H> ctx) {
        List<H> candles = ctx.getHistoricalData();
        if (candles.isEmpty()) {
            return Map.of();
        }

        BigDecimal peak = candles.stream()
                .findFirst().map(HistoricalData::price).orElse(BigDecimal.ZERO);

        BigDecimal mdd = BigDecimal.ZERO;

        for (HistoricalData candle : candles) {
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
