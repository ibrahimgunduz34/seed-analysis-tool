package org.seed.fund.report.calculator;

import org.seed.fund.report.model.ReportContext;
import org.seed.util.BigDecimalMath;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.function.Function;

public class StandardDeviationCalculator implements Function<ReportContext, ReportContext> {
    @Override
    public ReportContext apply(ReportContext ctx) {
        BigDecimal total = ctx.getDailyChanges().stream()
                .reduce(BigDecimal.ZERO, BigDecimal::add);

        int n = ctx.getDailyChanges().size();

        if (n <= 1) {
            ctx.setDailyStandardDeviation(BigDecimal.ZERO);
            ctx.setStandardDeviation(BigDecimal.ZERO);
            return ctx;
        }

        BigDecimal mean = total.divide(
                BigDecimal.valueOf(n),
                10,
                RoundingMode.HALF_UP
        );

        BigDecimal sumSquaredDiffs = ctx.getDailyChanges().stream()
                .map(v -> v.subtract(mean).pow(2))
                .reduce(BigDecimal.ZERO, BigDecimal::add);

        BigDecimal variance = sumSquaredDiffs.divide(
                BigDecimal.valueOf(n - 1),
                10,
                RoundingMode.HALF_UP
        );

        BigDecimal sampleStDev = BigDecimalMath.sqrt(variance, 10);

        ctx.setDailyStandardDeviation(sampleStDev);

        BigDecimal periodStDev = sampleStDev
                .multiply(BigDecimalMath.sqrt(BigDecimal.valueOf(n), 10));

        ctx.setStandardDeviation(periodStDev);

        return ctx;
    }


}
