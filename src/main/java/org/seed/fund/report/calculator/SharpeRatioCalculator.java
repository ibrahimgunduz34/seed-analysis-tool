package org.seed.fund.report.calculator;

import org.seed.fund.report.model.ReportContext;
import org.seed.util.BigDecimalMath;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.List;
import java.util.function.Function;

public class SharpeRatioCalculator implements Function<ReportContext, ReportContext> {

    private static final int ANNUAL_TRADING_DAYS = 252; // Yıllık iş günü sayısı

    @Override
    public ReportContext apply(ReportContext ctx) {
        List<BigDecimal> dailyChanges = ctx.getDailyChanges();
        if (dailyChanges == null || dailyChanges.isEmpty()) {
            ctx.setSharpeRatio(BigDecimal.ZERO);
            return ctx;
        }

        // Günlük ortalama getiri
        BigDecimal meanDaily = dailyChanges.stream()
                .reduce(BigDecimal.ZERO, BigDecimal::add)
                .divide(BigDecimal.valueOf(dailyChanges.size()), 10, RoundingMode.HALF_UP);

        // Günlük standart sapma (önceden hesaplanmış)
        BigDecimal dailyStDev = ctx.getDailyStandardDeviation();
        if (dailyStDev == null || dailyStDev.compareTo(BigDecimal.ZERO) == 0) {
            ctx.setSharpeRatio(BigDecimal.ZERO);
            return ctx;
        }

        // Yıllık Sharpe: mean / std * sqrt(252)
        BigDecimal sharpeAnnual = meanDaily
                .divide(dailyStDev, 10, RoundingMode.HALF_UP)
                .multiply(BigDecimalMath.sqrt(BigDecimal.valueOf(ANNUAL_TRADING_DAYS), 10));

        ctx.setSharpeRatio(sharpeAnnual);
        return ctx;
    }
}
