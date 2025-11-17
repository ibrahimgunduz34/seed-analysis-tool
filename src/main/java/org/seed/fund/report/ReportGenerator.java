package org.seed.fund.report;

import org.seed.fund.model.Fund;
import org.seed.fund.model.FundHistoricalData;
import org.seed.fund.report.calculator.*;
import org.seed.fund.report.model.ReportContext;
import org.seed.fund.storage.FundStorage;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.time.Duration;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.function.Function;

@Component
public class ReportGenerator {
    private final FundStorage fundStorage;

    public ReportGenerator(FundStorage fundStorage) {
        this.fundStorage = fundStorage;
    }

    public List<ReportContext> generate(
            List<String> codes,
            LocalDate beginDate,
            LocalDate endDate,
            BigDecimal initialAmount,
            Integer frequency
    ) {
        return codes.stream()
                .map(code -> calculateReportContext(code, beginDate, endDate, initialAmount, frequency))
                .flatMap(Optional::stream)
                .toList();
    }

    private Optional<ReportContext> calculateReportContext(
            String code,
            LocalDate beginDate,
            LocalDate endDate,
            BigDecimal initialAmount,
            Integer frequency
    ) {
        Fund fund = fundStorage.getFundByCode(code);
        List<FundHistoricalData> fundHistoricalDataList = fundStorage.getHistoricalDataByDateRange(fund.getFundMetaData().getCode(), beginDate, endDate);

        if (fundHistoricalDataList.isEmpty() || Duration.between(beginDate.atStartOfDay(), endDate.atStartOfDay()).toDays() < 30) {
            return Optional.empty();
        }

        LocalDate firstAvailableDate = fundHistoricalDataList.get(0).getValueDate();
        long gapDays = Duration.between(beginDate.atStartOfDay(), firstAvailableDate.atStartOfDay()).toDays();

        long toleranceDays = 30;
        if (gapDays > toleranceDays) {
            return Optional.empty();
        }


        ReportContext ctx = new ReportContext(fund.getFundMetaData(), fundHistoricalDataList, beginDate, endDate);

        Function<ReportContext, ReportContext> pipeline = new DailyChangeCalculator()
                .andThen(new PositiveNegativeDaysCalculator())
                .andThen(new MeanCalculator())
                .andThen(new GainLossCalculator())
                .andThen(new StandardDeviationCalculator())
                .andThen(new SharpeRatioCalculator())
                .andThen(new PriceChangeCalculator())
                .andThen(new MaxDrawdownCalculator())
                .andThen(new SortinoCalculator());

        return Optional.of(pipeline.apply(ctx));
    }
}
