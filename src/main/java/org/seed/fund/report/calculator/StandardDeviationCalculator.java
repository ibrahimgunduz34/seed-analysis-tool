package org.seed.fund.report.calculator;

import org.seed.fund.report.model.ReportContext;
import org.seed.util.BigDecimalMath;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.function.Function;

public class StandardDeviationCalculator implements Function<ReportContext, ReportContext> {

    @Override
    public ReportContext apply(ReportContext ctx) {
        if (ctx.getNumberOfDays() == 0) {
            ctx.setStandardDeviation(BigDecimal.ZERO);
            return ctx;
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

        return ctx;
    }


}
