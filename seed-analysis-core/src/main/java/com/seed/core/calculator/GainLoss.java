package com.seed.core.calculator;

import com.seed.core.AnalysisContext;
import com.seed.core.model.Candle;
import com.seed.core.model.ResultKey;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import static com.seed.core.calculator.DailyPriceChange.DAILY_PRICE_CHANGE;
import static com.seed.core.calculator.PositiveNegativeDays.NUMBER_OF_NEGATIVE_DAYS;
import static com.seed.core.calculator.PositiveNegativeDays.NUMBER_OF_POSITIVE_DAYS;

public class GainLoss<C extends Candle> implements Calculator<C> {
    public static final ResultKey<BigDecimal> AVERAGE_GAIN = ResultKey.of("Average Gain", BigDecimal.class);
    public static final ResultKey<BigDecimal> AVERAGE_LOSS = ResultKey.of("Average Loss", BigDecimal.class);

    @Override
    public List<ResultKey<?>> requires() {
        return List.of(
                DAILY_PRICE_CHANGE,
                NUMBER_OF_NEGATIVE_DAYS,
                NUMBER_OF_POSITIVE_DAYS
        );
    }

    @Override
    public List<ResultKey<?>> produces() {
        return List.of(
                AVERAGE_GAIN,
                AVERAGE_LOSS
        );
    }

    @Override
    public Map<ResultKey<?>, Object> calculate(AnalysisContext<?, C> ctx) {
        Optional<List<BigDecimal>> dailyPriceChangesOpt = ctx.get(DAILY_PRICE_CHANGE);

        if (dailyPriceChangesOpt.isEmpty()) {
            return Map.of();
        }

        List<BigDecimal> dailyPriceChanges = dailyPriceChangesOpt.get();
        int numberOfPositiveDays = ctx.get(NUMBER_OF_POSITIVE_DAYS).orElse(0);
        int numberOfNegativeDays = ctx.get(NUMBER_OF_NEGATIVE_DAYS).orElse(0);

        BigDecimal totalGain = dailyPriceChanges.stream()
                .filter(change -> change.signum() > 0)
                .reduce(BigDecimal.ZERO, BigDecimal::add);

        BigDecimal totalLoss = dailyPriceChanges.stream()
                .filter(change -> change.signum() < 0)
                .map(BigDecimal::abs)
                .reduce(BigDecimal.ZERO, BigDecimal::add);

        BigDecimal averageGain = numberOfPositiveDays > 0 ? totalGain.divide(
                BigDecimal.valueOf(numberOfPositiveDays), 10,  RoundingMode.HALF_UP)
                : BigDecimal.ZERO;

        BigDecimal averageLoss = numberOfNegativeDays > 0 ? totalLoss.divide(
                BigDecimal.valueOf(numberOfNegativeDays), 10, RoundingMode.HALF_UP)
                : BigDecimal.ZERO;

        return buildContext(averageGain, averageLoss);
    }

    private Map<ResultKey<?>, Object> buildContext(BigDecimal averageGain, BigDecimal averageLoss) {
        return Map.of(
                AVERAGE_GAIN, averageGain,
                AVERAGE_LOSS, averageLoss
        );
    }
}
