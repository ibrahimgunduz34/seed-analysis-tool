package com.seed.fund.command;

import com.seed.core.AnalysisContext;
import com.seed.core.AssetAnalyzer;
import com.seed.core.printer.CompositePrinter;
import com.seed.core.printer.ReportHeader;
import com.seed.core.printer.InfoTable;
import com.seed.fund.model.FundHistoricalData;
import com.seed.fund.model.FundMetaData;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.util.List;
import java.util.stream.Stream;

@Component
@ConditionalOnProperty(name = "task", havingValue = "PeriodComparisonReport")
public class PeriodComparisonReport implements CommandLineRunner {
    private final AssetAnalyzer<FundMetaData, FundHistoricalData> assetAnalyzer;

    public PeriodComparisonReport(AssetAnalyzer<FundMetaData, FundHistoricalData> assetAnalyzer) {
        this.assetAnalyzer = assetAnalyzer;
    }

    @Override
    public void run(String... args) throws Exception {
        String[] codes = args[0].split(",");

        LocalDate endDate = LocalDate.now();
        LocalDate startDate = endDate.minusMonths(1);

        List<AnalysisContext<FundMetaData, FundHistoricalData>> contexts = Stream.of(codes)
                .map(code -> assetAnalyzer.analyze(code, startDate, endDate))
                .toList();

        CompositePrinter printer = new CompositePrinter(
                new ReportHeader(),
                new InfoTable()
        );

        printer.print(contexts);

        System.exit(0);
    }
}
