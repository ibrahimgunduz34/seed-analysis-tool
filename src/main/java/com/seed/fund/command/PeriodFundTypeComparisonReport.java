package com.seed.fund.command;

import com.seed.configuration.ReportConfiguration;
import com.seed.core.AnalysisContext;
import com.seed.core.AssetAnalyzer;
import com.seed.core.printer.CompositePrinter;
import com.seed.core.printer.InfoTable;
import com.seed.core.printer.PerformanceChart;
import com.seed.core.printer.ReportHeader;
import com.seed.core.storage.MetaDataStorage;
import com.seed.fund.model.FundHistoricalData;
import com.seed.fund.model.FundMetaData;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.util.List;
import java.util.Objects;
import java.util.stream.Stream;

@Component
@ConditionalOnProperty(name = "task", havingValue = "PeriodFundTypeComparisonReport")
public class PeriodFundTypeComparisonReport implements ApplicationRunner {
    private final AssetAnalyzer<FundMetaData, FundHistoricalData> assetAnalyzer;
    private final ReportConfiguration reportConfig;
    private final MetaDataStorage<FundMetaData> metaDataStorage;

    public PeriodFundTypeComparisonReport(AssetAnalyzer<FundMetaData,
            FundHistoricalData> assetAnalyzer, ReportConfiguration reportConfig,
                                          MetaDataStorage<FundMetaData> metaDataStorage) {
        this.assetAnalyzer = assetAnalyzer;
        this.reportConfig = reportConfig;
        this.metaDataStorage = metaDataStorage;
    }

    @Override
    public void run(ApplicationArguments args) throws Exception {
        String sFundType = args.getNonOptionArgs().get(0);

        FundTypeEnum fundType = FundTypeEnum.valueOf(sFundType);

        LocalDate endDate = LocalDate.now();

        Stream<LocalDate> startDates = Stream.of(endDate.minusMonths(1),
                endDate.minusMonths(3),
                endDate.minusMonths(6),
                endDate.minusMonths(12));

        List<String> codes = metaDataStorage.getAllMetaData()
                .stream()
                .filter(item -> item.fundType().equals(fundType.getValue()))
                .map(FundMetaData::code)
                .toList();

        startDates.forEach(startDate -> printReport(codes, startDate, endDate));

        System.exit(0);
    }

    private void printReport(List<String> codes, LocalDate startDate, LocalDate endDate) {
        List<AnalysisContext<FundMetaData, FundHistoricalData>> contexts = codes.stream()
                .map(code -> {
                    try {
                        return assetAnalyzer.analyze(code, startDate, endDate);
                    } catch (Exception e) {
                        return null;
                    }
                })
                .filter(Objects::nonNull)
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
