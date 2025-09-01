package org.seed.fund.report.calculator;

import org.seed.fund.report.model.ReportContext;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.List;
import java.util.function.Function;

public class IncomePerformanceCalculator implements Function<ReportContext, ReportContext> {
    @Override
    public ReportContext apply(ReportContext ctx) {
        double positivePerformance = (double) ctx.getPositiveIncome() / (ctx.getPositiveIncome() + ctx.getNegativeIncome()) * 100;
        double negativePerformance = (double) ctx.getNegativeIncome() / (ctx.getPositiveIncome() + ctx.getNegativeIncome()) * 100;

        List<BigDecimal> changes = ctx.getDailyChanges();

        BigDecimal averageGain = changes.stream()
                .filter(c -> c.signum() > 0)
                .reduce(BigDecimal.ZERO, BigDecimal::add)
                .divide(BigDecimal.valueOf(ctx.getPositiveIncome()), 10, RoundingMode.HALF_UP);

        BigDecimal averageLoss = changes.stream()
                .filter(c -> c.signum() < 0)
                .map(BigDecimal::abs)
                .reduce(BigDecimal.ZERO, BigDecimal::add)
                .divide(BigDecimal.valueOf(ctx.getNegativeIncome()), 10, RoundingMode.HALF_UP);

        ctx.setPositiveIncomePerformance(positivePerformance);
        ctx.setNegativeIncomePerformance(negativePerformance);
        ctx.setAverageGain(averageGain);
        ctx.setAverageLoss(averageLoss);

        return ctx;
    }
}
