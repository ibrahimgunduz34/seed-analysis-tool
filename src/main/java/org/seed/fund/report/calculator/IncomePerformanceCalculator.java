package org.seed.fund.report.calculator;

import org.seed.fund.report.model.ReportContext;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.List;
import java.util.function.Function;

public class IncomePerformanceCalculator implements Function<ReportContext, ReportContext> {
    @Override
    public ReportContext apply(ReportContext ctx) {
        long totalDays = ctx.getPositiveIncome() + ctx.getNegativeIncome();
        double positivePerformance = totalDays > 0 ? (double) ctx.getPositiveIncome() / totalDays * 100 : 0.0;
        double negativePerformance = totalDays > 0 ? (double) ctx.getNegativeIncome() / totalDays * 100 : 0.0;

        List<BigDecimal> changes = ctx.getDailyChanges();

        BigDecimal averageGain = ctx.getPositiveIncome() > 0
                ? changes.stream().filter(c -> c.signum() > 0).reduce(BigDecimal.ZERO, BigDecimal::add)
                .divide(BigDecimal.valueOf(ctx.getPositiveIncome()), 10, RoundingMode.HALF_UP)
                : BigDecimal.ZERO;

        BigDecimal averageLoss = ctx.getNegativeIncome() > 0
                ? changes.stream().filter(c -> c.signum() < 0).map(BigDecimal::abs)
                .reduce(BigDecimal.ZERO, BigDecimal::add)
                .divide(BigDecimal.valueOf(ctx.getNegativeIncome()), 10, RoundingMode.HALF_UP)
                : BigDecimal.ZERO;

        ctx.setPositiveIncomePerformance(positivePerformance);
        ctx.setNegativeIncomePerformance(negativePerformance);
        ctx.setAverageGain(averageGain);
        ctx.setAverageLoss(averageLoss);

        return ctx;
    }
}
