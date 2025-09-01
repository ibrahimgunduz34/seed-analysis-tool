package org.seed.fund.report;

import org.seed.util.TablePrinter;
import org.seed.fund.model.Fund;
import org.seed.fund.model.HistoricalData;
import org.seed.fund.report.calculator.*;
import org.seed.fund.report.model.ReportContext;
import org.seed.fund.storage.FundStorage;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.function.Function;

@Component
public class ReportGenerator {
    private final FundStorage fundStorage;

    public ReportGenerator(FundStorage fundStorage) {
        this.fundStorage = fundStorage;
    }

    public void generate(
            List<String> codes,
            LocalDate beginDate,
            LocalDate endDate,
            BigDecimal initialAmount,
            Integer frequency
    ) {
        List<ReportContext> contexts = codes.stream()
                .map(code -> calculateReportContext(code, beginDate, endDate, initialAmount, frequency))
                .toList();

        TablePrinter.print(contexts);
    }

    private ReportContext calculateReportContext(
            String code,
            LocalDate beginDate,
            LocalDate endDate,
            BigDecimal initialAmount,
            Integer frequency
    ) {
        Fund fund = fundStorage.getFundByCode(code);
        List<HistoricalData> historicalDataList = fundStorage.getHistoricalDataByDateRange(fund.getMetaData().getCode(), beginDate, endDate);

        ReportContext ctx = new ReportContext(fund.getMetaData(), historicalDataList);

        Function<ReportContext, ReportContext> pipeline = new DailyChangeCalculator()
                .andThen(new IncomeCalculator())
                .andThen(new IncomePerformanceCalculator())
                .andThen(new StandardDeviationCalculator())
                .andThen(new PriceChangeCalculator())
                .andThen(new SharpeRatioCalculator());

        return pipeline.apply(ctx);
    }
}
