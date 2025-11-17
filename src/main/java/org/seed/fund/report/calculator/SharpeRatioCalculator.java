package org.seed.fund.report.calculator;

import org.seed.fund.report.model.ReportContext;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.function.Function;

public class SharpeRatioCalculator implements Function<ReportContext, ReportContext> {

    @Override
    public ReportContext apply(ReportContext ctx) {
        if (ctx.getNumberOfDays() == 0) {
            ctx.setSharpeRatio(BigDecimal.ZERO);
            return ctx;
        }

        BigDecimal sharpe = ctx.getStandardDeviation().compareTo(BigDecimal.ZERO) > 0
                ? ctx.getMean().divide(ctx.getStandardDeviation(), 10, RoundingMode.HALF_UP)
                : BigDecimal.ZERO;

        ctx.setSharpeRatio(sharpe);

        return ctx;
    }
}
