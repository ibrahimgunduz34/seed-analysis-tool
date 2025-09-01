package org.seed.fund.report.calculator;

import org.seed.fund.model.HistoricalData;
import org.seed.fund.report.ReportGenerator;
import org.seed.fund.report.model.ReportContext;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.List;
import java.util.function.Function;
import java.util.stream.IntStream;

public class DailyChangeCalculator implements Function<ReportContext, ReportContext> {
    @Override
    public ReportContext apply(ReportContext ctx) {
        List<HistoricalData> historicalDataList = ctx.getHistoricalDataList();
        List<BigDecimal> changes = IntStream.range(1, historicalDataList.size())
                .mapToObj(i -> {
                    BigDecimal prev = historicalDataList.get(i - 1).getPrice();
                    BigDecimal current = historicalDataList.get(i).getPrice();
                    return current.subtract(prev).divide(
                            prev,
                            10,
                            RoundingMode.HALF_UP
                    );
                })
                .toList();

        ctx.setDailyChanges(changes);

        return ctx;
    }
}
