package org.seed.fund.report.calculator;

import org.seed.fund.report.model.ReportContext;
import org.seed.util.BigDecimalMath;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.function.Function;

public class StandardDeviationCalculator implements Function<ReportContext, ReportContext> {

    private static final int ANNUAL_TRADING_DAYS = 252; // Yıllık iş günü sayısı

    @Override
    public ReportContext apply(ReportContext ctx) {
        if (ctx.getNumberOfDays() == 0) {
            ctx.setStandardDeviation(BigDecimal.ZERO);
            ctx.setMean(BigDecimal.ZERO);
            return ctx;
        }

        BigDecimal mean = ctx.getDailyChanges()
                .stream().reduce(BigDecimal.ZERO, BigDecimal::add)
                .divide(BigDecimal.valueOf(ctx.getNumberOfDays()), RoundingMode.HALF_UP);

        BigDecimal sumSquaredDiffs = ctx.getDailyChanges().stream()
                .map(v -> v.subtract(mean).pow(2))
                .reduce(BigDecimal.ZERO, BigDecimal::add);

        BigDecimal variance = sumSquaredDiffs.divide(
                BigDecimal.valueOf(ctx.getNumberOfDays() - 1),
                10,
                RoundingMode.HALF_UP);

        BigDecimal sampleStDev = BigDecimalMath.sqrt(variance, 10);
        BigDecimal stDev = sampleStDev.multiply(BigDecimalMath.sqrt(BigDecimal.valueOf(ANNUAL_TRADING_DAYS), 10));

        ctx.setMean(mean);
        ctx.setStandardDeviation(stDev);

        return ctx;
    }


}
