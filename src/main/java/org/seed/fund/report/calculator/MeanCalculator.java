package org.seed.fund.report.calculator;

import org.seed.fund.report.model.ReportContext;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.function.Function;

public class MeanCalculator implements Function<ReportContext, ReportContext> {

    @Override
    public ReportContext apply(ReportContext ctx) {
        if (ctx.getNumberOfDays() == 0) {
            ctx.setMean(BigDecimal.ZERO);
            return ctx;
        }

        BigDecimal mean = ctx.getDailyChanges()
                .stream().reduce(BigDecimal.ZERO, BigDecimal::add)
                .divide(BigDecimal.valueOf(ctx.getNumberOfDays()), RoundingMode.HALF_UP);

        if (mean.compareTo(BigDecimal.ZERO) == 0) {
            ctx.setMean(BigDecimal.ZERO);
            return ctx;
        }

        ctx.setMean(mean);
        return ctx;
    }
}
