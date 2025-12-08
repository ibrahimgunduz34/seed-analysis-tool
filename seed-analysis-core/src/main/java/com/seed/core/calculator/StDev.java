package com.seed.core.calculator;

import com.seed.core.AnalysisContext;
import com.seed.core.model.Candle;
import com.seed.core.model.ResultKey;
import com.seed.core.util.BigDecimalMath;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import static com.seed.core.calculator.DailyPriceChange.DAILY_PRICE_CHANGE;
import static com.seed.core.calculator.Mean.MEAN;

public class StDev<C extends Candle> implements Calculator<C> {
    public static final ResultKey<BigDecimal> ST_DEV = ResultKey.of("StDev", BigDecimal.class);

    @Override
    public List<ResultKey<?>> requires() {
        return List.of(
                DAILY_PRICE_CHANGE,
                MEAN
        );
    }

    @Override
    public List<ResultKey<?>> produces() {
        return List.of(
                ST_DEV
        );
    }

    @Override
    public Map<ResultKey<?>, Object> calculate(AnalysisContext<?, C> ctx) {
        Optional<List<BigDecimal>> dailyPriceChangesOpt = ctx.get(DAILY_PRICE_CHANGE);

        if (dailyPriceChangesOpt.isEmpty()) {
            return Map.of();
        }

        List<BigDecimal> dailyPriceChanges = dailyPriceChangesOpt.get();
        BigDecimal mean = ctx.get(MEAN).orElse(BigDecimal.ZERO);

        BigDecimal sumSquaredDiffs = dailyPriceChanges.stream()
                .map(v -> v.subtract(mean).pow(2))
                .reduce(BigDecimal.ZERO, BigDecimal::add);

        BigDecimal variance = sumSquaredDiffs.divide(
                BigDecimal.valueOf(dailyPriceChanges.size() - 1),
                10,
                RoundingMode.HALF_UP);

        BigDecimal sampleStDev = BigDecimalMath.sqrt(variance, 10);

        return Map.of(
                ST_DEV, sampleStDev
        );
    }
}
