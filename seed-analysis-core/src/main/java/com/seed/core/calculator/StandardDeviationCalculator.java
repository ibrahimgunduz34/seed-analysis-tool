package com.seed.core.calculator;

import com.seed.core.CalculationContext;
import com.seed.core.util.BigDecimalMath;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.math.RoundingMode;

@Component
public class StandardDeviationCalculator implements Calculator {
    @Override
    public void calculate(CalculationContext ctx) {
        if (ctx.getNumberOfDays() == 0) {
            ctx.setStandardDeviation(BigDecimal.ZERO);
            return;
        }

        BigDecimal sumSquaredDiffs = ctx.getDailyChanges().stream()
                .map(v -> v.subtract(ctx.getMean()).pow(2))
                .reduce(BigDecimal.ZERO, BigDecimal::add);

        BigDecimal variance = sumSquaredDiffs.divide(
                BigDecimal.valueOf(ctx.getNumberOfDays() - 1),
                10,
                RoundingMode.HALF_UP);

        BigDecimal sampleStDev = BigDecimalMath.sqrt(variance, 10);

        ctx.setStandardDeviation(sampleStDev);
    }
}
