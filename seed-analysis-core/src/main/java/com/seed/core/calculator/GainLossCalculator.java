package com.seed.core.calculator;

import com.seed.core.CalculationContext;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.math.RoundingMode;

@Component
public class GainLossCalculator implements Calculator {
    @Override
    public void calculate(CalculationContext ctx) {
        if (ctx.getNumberOfDays() == 0) {
            ctx.setAverageGain(BigDecimal.ZERO);
            ctx.setAverageLoss(BigDecimal.ZERO);
            return;
        }

        BigDecimal totalGain = ctx.getDailyChanges().stream()
                .filter(change -> change.signum() > 0)
                .reduce(BigDecimal.ZERO, BigDecimal::add);

        BigDecimal totalLoss = ctx.getDailyChanges().stream()
                .filter(change -> change.signum() < 0)
                .map(BigDecimal::abs)
                .reduce(BigDecimal.ZERO, BigDecimal::add);

        BigDecimal averageGain = ctx.getNumberOfPositiveDays() > 0 ? totalGain.divide(
                BigDecimal.valueOf(ctx.getNumberOfPositiveDays()), 10,  RoundingMode.HALF_UP)
                : BigDecimal.ZERO;

        BigDecimal averageLoss = ctx.getNumberOfNegativeDays() > 0 ? totalLoss.divide(
                BigDecimal.valueOf(ctx.getNumberOfNegativeDays()), 10, RoundingMode.HALF_UP)
                : BigDecimal.ZERO;

        ctx.setAverageGain(averageGain);
        ctx.setAverageLoss(averageLoss);
    }
}
