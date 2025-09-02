package org.seed.fund.report.calculator;

import org.seed.fund.model.HistoricalData;
import org.seed.fund.report.model.ReportContext;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.List;
import java.util.function.Function;

public class MaxDrawdownCalculator implements Function<ReportContext, ReportContext> {

    @Override
    public ReportContext apply(ReportContext ctx) {
        List<HistoricalData> historicalDataList = ctx.getHistoricalDataList();

        if (historicalDataList == null || historicalDataList.isEmpty()) {
            ctx.setMaxDrawdown(BigDecimal.ZERO);
            return ctx;
        }

        BigDecimal peak = historicalDataList.get(0).getPrice();
        BigDecimal maxDrawdown = BigDecimal.ZERO;

        for (HistoricalData historicalData : historicalDataList) {
            BigDecimal price = historicalData.getPrice();
            if (price.compareTo(peak) > 0) {
                peak = price;
            }
            // Drawdown = (CurrentPrice / Peak) - 1
            BigDecimal drawdown = price.divide(peak, 10, RoundingMode.HALF_UP).subtract(BigDecimal.ONE);

            if (drawdown.compareTo(maxDrawdown) < 0) {
                maxDrawdown = drawdown;
            }
        }

        ctx.setMaxDrawdown(maxDrawdown); // Ham veri olarak saklanıyor, raporda *100 ile gösterilecek
        return ctx;
    }
}
