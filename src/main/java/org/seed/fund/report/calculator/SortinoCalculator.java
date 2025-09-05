package org.seed.fund.report.calculator;

import org.seed.fund.report.model.ReportContext;
import org.seed.util.BigDecimalMath;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.function.Function;

public class SortinoCalculator implements Function<ReportContext, ReportContext> {

    private static final int ANNUAL_TRADING_DAYS = 252; // Yıllık iş günü sayısı
    private static final BigDecimal TARGET_RETURN = BigDecimal.ZERO; // Hedef getiri: 0 veya risk-free rate

    @Override
    public ReportContext apply(ReportContext ctx) {
        if (ctx.getNumberOfDays() == 0) {
            ctx.setSortinoRatio(BigDecimal.ZERO);
            return ctx;
        }

        // Negatif sapmaların karelerinin toplamı (target altında olanlar)
        BigDecimal sumSquaredDownside = ctx.getDailyChanges().stream()
                .filter(r -> r.compareTo(TARGET_RETURN) < 0)
                .map(r -> TARGET_RETURN.subtract(r).pow(2))
                .reduce(BigDecimal.ZERO, BigDecimal::add);

        // Negatif gün sayısı ReportContext'ten alınıyor
        int nNegative = ctx.getNumberOfNegativeDays();

        // Downside varyans
        BigDecimal downsideVariance = nNegative > 1
                ? sumSquaredDownside.divide(BigDecimal.valueOf(nNegative - 1), 10, RoundingMode.HALF_UP)
                : BigDecimal.ZERO;

        // Yıllıklaştırılmış downside standart sapma
        BigDecimal downsideStDev = BigDecimalMath.sqrt(downsideVariance, 10)
                .multiply(BigDecimalMath.sqrt(BigDecimal.valueOf(ANNUAL_TRADING_DAYS), 10));

        // Yıllıklaştırılmış ortalama getiri (önceki Mean kullanılıyor)
        BigDecimal annualizedMean = ctx.getMean() != null
                ? ctx.getMean().multiply(BigDecimal.valueOf(ANNUAL_TRADING_DAYS))
                : BigDecimal.ZERO;

        // Sortino oranı
        BigDecimal sortino = downsideStDev.compareTo(BigDecimal.ZERO) > 0
                ? annualizedMean.divide(downsideStDev, 10, RoundingMode.HALF_UP)
                : BigDecimal.ZERO;

        ctx.setSortinoRatio(sortino);
        return ctx;
    }
}
