package org.seed.fund.report.calculator;

import org.seed.fund.report.model.ReportContext;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.function.Function;

public class SharpeRatioCalculator implements Function<ReportContext, ReportContext> {

    private static final int ANNUAL_TRADING_DAYS = 252; // Yıllık iş günü sayısı

    @Override
    public ReportContext apply(ReportContext ctx) {
        if (ctx.getNumberOfDays() == 0) {
            ctx.setSharpeRatio(BigDecimal.ZERO);
            return ctx;
        }

        BigDecimal annualizedMean = ctx.getMean().multiply(BigDecimal.valueOf(ANNUAL_TRADING_DAYS));

        BigDecimal sharpe = ctx.getStandardDeviation().compareTo(BigDecimal.ZERO) > 0
                ? annualizedMean.divide(ctx.getStandardDeviation(), 10, RoundingMode.HALF_UP)
                : BigDecimal.ZERO;

        ctx.setSharpeRatio(sharpe);

        return ctx;
    }
}
