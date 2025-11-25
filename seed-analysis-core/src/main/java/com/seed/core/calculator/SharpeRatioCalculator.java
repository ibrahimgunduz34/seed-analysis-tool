package com.seed.core.calculator;

import com.seed.core.CalculationContext;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.math.RoundingMode;

@Component
public class SharpeRatioCalculator implements Calculator {
    @Override
    public void calculate(CalculationContext ctx) {
        if (ctx.getNumberOfDays() == 0) {
            ctx.setSharpeRatio(BigDecimal.ZERO);
            return;
        }

        BigDecimal sharpe = ctx.getStandardDeviation().compareTo(BigDecimal.ZERO) > 0
                ? ctx.getMean().divide(ctx.getStandardDeviation(), 10, RoundingMode.HALF_UP)
                : BigDecimal.ZERO;

        ctx.setSharpeRatio(sharpe);
    }
}
