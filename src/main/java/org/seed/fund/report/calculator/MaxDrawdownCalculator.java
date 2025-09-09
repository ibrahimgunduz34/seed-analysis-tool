package org.seed.fund.report.calculator;

import org.seed.fund.model.FundHistoricalData;
import org.seed.fund.report.model.ReportContext;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.List;
import java.util.function.Function;

public class MaxDrawdownCalculator implements Function<ReportContext, ReportContext> {

    @Override
    public ReportContext apply(ReportContext ctx) {
        if (ctx.getNumberOfDays() == 0 || ctx.getMean().compareTo(BigDecimal.ZERO) == 0) {
            ctx.setMaxDrawdown(BigDecimal.ZERO);
            return ctx;
        }

        List<FundHistoricalData> fundHistoricalDataList = ctx.getFundHistoricalDataList();

        BigDecimal peak = fundHistoricalDataList.get(0).getPrice();
        BigDecimal maxDrawdown = BigDecimal.ZERO;

        for (FundHistoricalData fundHistoricalData : fundHistoricalDataList) {
            BigDecimal price = fundHistoricalData.getPrice();
            if (price.compareTo(peak) > 0) {
                peak = price;
            }

            if (peak.compareTo(BigDecimal.ZERO) == 0) {
                continue; // Peak sıfırsa drawdown hesaplanamaz, atla
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
