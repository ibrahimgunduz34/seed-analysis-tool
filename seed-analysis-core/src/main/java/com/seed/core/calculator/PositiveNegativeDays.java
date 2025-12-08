package com.seed.core.calculator;

import com.seed.core.AnalysisContext;
import com.seed.core.model.Candle;
import com.seed.core.model.ResultKey;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import static com.seed.core.calculator.DailyPriceChange.DAILY_PRICE_CHANGE;

public class PositiveNegativeDays<C extends Candle> implements Calculator<C> {
    public static final ResultKey<Integer> NUMBER_OF_POSITIVE_DAYS = ResultKey.of("Number Of Positive Days", Integer.class);
    public static final ResultKey<Integer> NUMBER_OF_NEGATIVE_DAYS = ResultKey.of("Number Of Negative Days", Integer.class);
    public static final ResultKey<Double> WEIGHT_OF_POSITIVE_DAYS = ResultKey.of("Weight Of Positive Days", Double.class);
    public static final ResultKey<Double> WEIGHT_OF_NEGATIVE_DAYS = ResultKey.of("Weight Of Negative Days", Double.class);

    @Override
    public List<ResultKey<?>> requires() {
        return List.of(
                DAILY_PRICE_CHANGE
        );
    }

    @Override
    public List<ResultKey<?>> produces() {
        return List.of(
                NUMBER_OF_NEGATIVE_DAYS,
                NUMBER_OF_POSITIVE_DAYS,
                WEIGHT_OF_NEGATIVE_DAYS,
                WEIGHT_OF_POSITIVE_DAYS
        );
    }

    @Override
    public Map<ResultKey<?>, Object> calculate(AnalysisContext<?, C> ctx) {
        Optional<List<BigDecimal>> dailyPriceChangesOpt = ctx.get(DAILY_PRICE_CHANGE);

        if (dailyPriceChangesOpt.isEmpty()) {
            return Map.of();
        }

        List<BigDecimal> dailyPriceChanges = dailyPriceChangesOpt.get();

        int numberOfPositiveDays = 0;
        int numberOfNegativeDays = 0;
        double weightOfPositiveDays = 0.0;
        double weightOfNegativeDays = 0.0;

        for (BigDecimal dailyChange : dailyPriceChanges) {
            if (dailyChange.signum() > 0) {
                numberOfPositiveDays++;
            } else if (dailyChange.signum() < 0) {
                numberOfNegativeDays++;
            }
        }

        int totalNonZeroDays = numberOfPositiveDays + numberOfNegativeDays;
        if (totalNonZeroDays > 0) {
            weightOfPositiveDays = (double) numberOfPositiveDays / totalNonZeroDays;
            weightOfNegativeDays = (double) numberOfNegativeDays / totalNonZeroDays;
        } else {
            weightOfPositiveDays = 0.0;
            weightOfNegativeDays = 0.0;
        }

        return buildContext(numberOfPositiveDays, numberOfNegativeDays, weightOfPositiveDays, weightOfNegativeDays);
    }

    private Map<ResultKey<?>, Object> buildContext(Integer noPositiveDays, Integer noNegativeDays, Double wPositiveDays, Double wNegativeDays) {
        return Map.of(
                NUMBER_OF_POSITIVE_DAYS, noPositiveDays,
                NUMBER_OF_NEGATIVE_DAYS, noNegativeDays,
                WEIGHT_OF_POSITIVE_DAYS, wPositiveDays,
                WEIGHT_OF_NEGATIVE_DAYS, wNegativeDays
        );
    }
}
