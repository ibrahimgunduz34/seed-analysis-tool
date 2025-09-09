package org.seed.fund.report.calculator;

import org.seed.fund.model.FundHistoricalData;
import org.seed.fund.report.model.ReportContext;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.List;
import java.util.function.Function;
import java.util.function.IntFunction;
import java.util.stream.IntStream;

public class DailyChangeCalculator implements Function<ReportContext, ReportContext> {
    @Override
    public ReportContext apply(ReportContext ctx) {
        List<FundHistoricalData> fundHistoricalDataList = ctx.getFundHistoricalDataList();
        List<BigDecimal> changes = IntStream.range(1, fundHistoricalDataList.size())
                .mapToObj(getBigDecimalIntFunction(fundHistoricalDataList))
                .toList();

        ctx.setDailyChanges(changes);
        ctx.setNumberOfDays(ctx.getFundHistoricalDataList().size());

        return ctx;
    }

    private static IntFunction<BigDecimal> getBigDecimalIntFunction(List<FundHistoricalData> fundHistoricalDataList) {
        return i -> {
            BigDecimal prev = fundHistoricalDataList.get(i - 1).getPrice();
            BigDecimal current = fundHistoricalDataList.get(i).getPrice();
            if (prev.compareTo(BigDecimal.ZERO) == 0) {
                return BigDecimal.ZERO;
            }
            return current.subtract(prev).divide(
                    prev,
                    10,
                    RoundingMode.HALF_UP
            );
        };
    }
}
