package org.seed.fund.report.calculator;

import org.seed.fund.report.model.ReportContext;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

public class IncomeCalculator implements Function<ReportContext, ReportContext> {
    @Override
    public ReportContext apply(ReportContext ctx) {
        List<BigDecimal> dailyChanges = ctx.getDailyChanges();

        Map<Boolean, Long> result = dailyChanges.stream()
                .collect(Collectors.groupingBy(c -> c.signum() > 0, Collectors.counting()));

        ctx.setPositiveIncome(result.get(true));
        ctx.setNegativeIncome(result.get(false));

        return ctx;
    }
}
