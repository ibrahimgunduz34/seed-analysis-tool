package com.seed.fund.command;

import com.seed.configuration.ReportConfiguration;
import com.seed.core.printer.PerformanceChart;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Component;
import com.seed.core.AnalysisContext;
import com.seed.core.AssetAnalyzer;
import com.seed.core.printer.CompositePrinter;
import com.seed.core.printer.InfoTable;
import com.seed.core.printer.ReportHeader;
import com.seed.fund.model.FundHistoricalData;
import com.seed.fund.model.FundMetaData;

import java.time.LocalDate;
import java.util.List;
import java.util.stream.Stream;

@Component
@ConditionalOnProperty(name = "task", havingValue = "PeriodComparisonReport")
public class PeriodComparisonReport implements CommandLineRunner {
    private final AssetAnalyzer<FundMetaData, FundHistoricalData> assetAnalyzer;
    private final ReportConfiguration reportConfig;

    public PeriodComparisonReport(AssetAnalyzer<FundMetaData, FundHistoricalData> assetAnalyzer, ReportConfiguration reportConfig) {
        this.assetAnalyzer = assetAnalyzer;
        this.reportConfig = reportConfig;
    }

    @Override
    public void run(String... args) throws Exception {
        String[] codes = args[0].split(",");

        LocalDate endDate = LocalDate.now();

        Stream<LocalDate> startDates = Stream.of(endDate.minusMonths(1),
                endDate.minusMonths(3),
                endDate.minusMonths(6),
                endDate.minusMonths(12));

        startDates.forEach(startDate -> printReport(codes, startDate, endDate));

        System.exit(0);
    }

    private void printReport(String[] codes, LocalDate startDate, LocalDate endDate) {
        List<AnalysisContext<FundMetaData, FundHistoricalData>> contexts = Stream.of(codes)
                .map(code -> assetAnalyzer.analyze(code, startDate, endDate))
                .toList();

        CompositePrinter printer = new CompositePrinter(
                new ReportHeader(),
                new InfoTable(),
                new PerformanceChart(reportConfig)
        );

        printer.print(contexts);
        System.out.println();
    }
}
