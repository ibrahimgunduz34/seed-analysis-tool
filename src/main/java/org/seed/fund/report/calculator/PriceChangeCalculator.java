package org.seed.fund.report.calculator;

import org.seed.fund.model.FundHistoricalData;
import org.seed.fund.report.model.ReportContext;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.List;
import java.util.function.Function;

public class PriceChangeCalculator implements Function<ReportContext,  ReportContext> {
    @Override
    public ReportContext apply(ReportContext ctx) {
        List<FundHistoricalData> fundHistoricalData = ctx.getFundHistoricalDataList();

        BigDecimal startPrice = fundHistoricalData.stream().findFirst().map(FundHistoricalData::getPrice).orElse(BigDecimal.ZERO);

        BigDecimal endPrice = fundHistoricalData.isEmpty()
                ? BigDecimal.ZERO
                : fundHistoricalData.get(fundHistoricalData.size() - 1).getPrice();


        BigDecimal change = BigDecimal.ZERO;
        if (startPrice.compareTo(BigDecimal.ZERO) != 0) {
            change = endPrice.subtract(startPrice)
                    .divide(startPrice, 10, RoundingMode.HALF_UP);
        }
        ctx.setPriceChange(change);

        return ctx;
    }
}
