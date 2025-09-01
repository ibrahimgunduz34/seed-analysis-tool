package org.seed.fund.report.calculator;

import org.seed.fund.report.model.ReportContext;
import org.seed.util.BigDecimalMath;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.List;
import java.util.function.Function;

public class SharpeRatioCalculator implements Function<ReportContext, ReportContext> {
    @Override
    public ReportContext apply(ReportContext ctx) {
        List<BigDecimal> changes = ctx.getDailyChanges();

        BigDecimal mean = changes.stream()
                .reduce(BigDecimal.ZERO, BigDecimal::add)
                .divide(BigDecimal.valueOf(changes.size()), 10, RoundingMode.HALF_UP);

        int annualWorkDays = 252;

        BigDecimal annualChange = mean.multiply(BigDecimal.valueOf(annualWorkDays));

        BigDecimal annualStDev = ctx.getDailyStandardDeviation().multiply(
                BigDecimalMath.sqrt(
                        BigDecimal.valueOf(annualWorkDays),
                        10
                )
        );

        BigDecimal sharpeRatio = annualChange.divide(
                annualStDev,
                10,
                RoundingMode.HALF_UP
        );

        ctx.setSharpeRatio(sharpeRatio);

        return ctx;
    }
}
