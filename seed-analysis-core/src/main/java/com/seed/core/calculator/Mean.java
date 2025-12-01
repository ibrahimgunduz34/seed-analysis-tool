package com.seed.core.calculator;

import com.seed.core.AnalysisContext;
import com.seed.core.Candle;
import com.seed.core.ResultKey;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import static com.seed.core.calculator.DailyPriceChange.DAILY_PRICE_CHANGE;

public class Mean<T extends Candle> implements Calculator<T> {
    public static final ResultKey<BigDecimal> MEAN = ResultKey.of("Mean", BigDecimal.class);

    @Override
    public List<ResultKey<?>> requires() {
        return List.of(DAILY_PRICE_CHANGE);
    }

    @Override
    public List<ResultKey<?>> produces() {
        return List.of(MEAN);
    }

    @Override
    public Map<ResultKey<?>, Object> calculate(AnalysisContext<T> ctx) {
        Optional<List<BigDecimal>> dailyPriceChangesOpt = ctx.get(DAILY_PRICE_CHANGE);

        if (dailyPriceChangesOpt.isEmpty()) {
            return Map.of();
        }

        List<BigDecimal> dailyPriceChanges = dailyPriceChangesOpt.get();

        BigDecimal mean = dailyPriceChanges
                .stream().reduce(BigDecimal.ZERO, BigDecimal::add)
                .divide(BigDecimal.valueOf(dailyPriceChanges.size()), RoundingMode.HALF_UP);

        if (mean.compareTo(BigDecimal.ZERO) == 0) {
            return Map.of(MEAN, BigDecimal.ZERO);
        }

        return Map.of(MEAN, mean);
    }
}
