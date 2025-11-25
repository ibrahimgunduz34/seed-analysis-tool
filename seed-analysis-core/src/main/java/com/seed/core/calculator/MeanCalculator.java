package com.seed.core.calculator;

import com.seed.core.CalculationContext;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.math.RoundingMode;

@Component
public class MeanCalculator implements Calculator {
    @Override
    public void calculate(CalculationContext ctx) {
        if (ctx.getNumberOfDays() == 0) {
            ctx.setMean(BigDecimal.ZERO);
            return;
        }

        BigDecimal mean = ctx.getDailyChanges()
                .stream().reduce(BigDecimal.ZERO, BigDecimal::add)
                .divide(BigDecimal.valueOf(ctx.getNumberOfDays()), RoundingMode.HALF_UP);

        if (mean.compareTo(BigDecimal.ZERO) == 0) {
            ctx.setMean(BigDecimal.ZERO);
            return;
        }

        ctx.setMean(mean);
    }
}
