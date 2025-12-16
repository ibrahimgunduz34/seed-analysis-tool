package com.seed.core.calculator;

import com.seed.core.AnalysisContext;
import com.seed.core.model.HistoricalData;
import com.seed.core.model.ResultKey;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.List;
import java.util.Map;
import java.util.function.IntFunction;
import java.util.stream.IntStream;

public class DailyPriceChange<H extends HistoricalData> implements Calculator<H> {

    public static final ResultKey<List<BigDecimal>> DAILY_PRICE_CHANGE =
            ResultKey.of("Daily.Changes", (Class<List<BigDecimal>>)(Class)List.class);

    @Override
    public List<ResultKey<?>> requires() {
        return List.of();
    }

    @Override
    public List<ResultKey<?>> produces() {
        return List.of(DAILY_PRICE_CHANGE);
    }

    @Override
    public Map<ResultKey<?>, Object> calculate(AnalysisContext<?, H> ctx) {
        List<H> candles = ctx.getHistoricalData();

        List<BigDecimal> dailyPriceChanges = IntStream.range(1, candles.size())
                .mapToObj(compute(candles))
                .toList();

        return Map.of(DAILY_PRICE_CHANGE, List.copyOf(dailyPriceChanges));
    }

    private IntFunction<BigDecimal> compute(List<H> candles) {
        return i -> {
            BigDecimal prev = candles.get(i - 1).price();
            BigDecimal current = candles.get(i).price();
            if (prev.compareTo(BigDecimal.ZERO) < 0) {
                return BigDecimal.ZERO;
            }
            return current.subtract(prev)
                    .divide(prev, 10, RoundingMode.HALF_UP);
        };
    }
}
