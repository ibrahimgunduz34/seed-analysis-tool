package com.seed.core.calculator;

import com.seed.core.AnalysisContext;
import com.seed.core.model.HistoricalData;
import com.seed.core.model.ResultKey;
import com.seed.core.util.BigDecimalMath;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import static com.seed.core.calculator.DailyPriceChange.DAILY_PRICE_CHANGE;
import static com.seed.core.calculator.Mean.MEAN;
import static com.seed.core.calculator.PositiveNegativeDays.NUMBER_OF_NEGATIVE_DAYS;

public class Sortino<H extends HistoricalData> implements Calculator<H> {
    public static final ResultKey<BigDecimal> SORTINO = ResultKey.of("Sortino", BigDecimal.class);

    private static final BigDecimal TARGET_RETURN = BigDecimal.ZERO;

    @Override
    public List<ResultKey<?>> requires() {
        return List.of(
                DAILY_PRICE_CHANGE,
                NUMBER_OF_NEGATIVE_DAYS,
                MEAN
        );
    }

    @Override
    public List<ResultKey<?>> produces() {
        return List.of(
                SORTINO
        );
    }

    @Override
    public Map<ResultKey<?>, Object> calculate(AnalysisContext<?, H> ctx) {
        Optional<List<BigDecimal>> dailyPriceChangesOpt = ctx.get(DAILY_PRICE_CHANGE);

        if (dailyPriceChangesOpt.isEmpty()) {
            return Map.of();
        }

        List<BigDecimal> dailyPriceChanges = dailyPriceChangesOpt.get();
        int nNegative = ctx.get(NUMBER_OF_NEGATIVE_DAYS).orElse(0);
        BigDecimal mean = ctx.get(MEAN).orElse(BigDecimal.ZERO);

        BigDecimal sumSquaredDownside = dailyPriceChanges.stream()
                .filter(r -> r.compareTo(TARGET_RETURN) < 0)
                .map(r -> TARGET_RETURN.subtract(r).pow(2))
                .reduce(BigDecimal.ZERO, BigDecimal::add);

        BigDecimal downsideVariance = nNegative > 1
                ? sumSquaredDownside.divide(BigDecimal.valueOf(nNegative - 1), 10, RoundingMode.HALF_UP)
                : BigDecimal.ZERO;

        BigDecimal downsideStDev = BigDecimalMath.sqrt(downsideVariance, 10);

        BigDecimal sortino = downsideStDev.compareTo(BigDecimal.ZERO) > 0
                ? mean.divide(downsideStDev, 10, RoundingMode.HALF_UP)
                : BigDecimal.ZERO;

        return Map.of(
                SORTINO, sortino
        );
    }
}
