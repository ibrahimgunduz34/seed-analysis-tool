package com.seed.fund.command;

import com.seed.configuration.ReportConfiguration;
import com.seed.core.AnalysisContext;
import com.seed.core.printer.snapshot.CompositePrinter;
import com.seed.core.printer.snapshot.InfoTable;
import com.seed.core.printer.snapshot.PerformanceChart;
import com.seed.core.printer.snapshot.ReportHeader;
import com.seed.core.report.ReportService;
import com.seed.core.storage.MetaDataStorage;
import com.seed.fund.model.FundHistoricalData;
import com.seed.fund.model.FundMetaData;
import com.seed.fund.model.FundTypeEnum;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.util.List;

@Component
@ConditionalOnProperty(name = "task", havingValue = "PeriodFundTypeComparisonReport")
public class PeriodFundTypeComparisonReport implements ApplicationRunner {
    private final ReportService<FundMetaData, FundHistoricalData> reportService;
    private final ReportConfiguration reportConfig;
    private final MetaDataStorage<FundMetaData> metaDataStorage;

    public PeriodFundTypeComparisonReport(ReportService<FundMetaData, FundHistoricalData> reportService,
                                          ReportConfiguration reportConfig,
                                          MetaDataStorage<FundMetaData> metaDataStorage) {
        this.reportService = reportService;

        this.reportConfig = reportConfig;
        this.metaDataStorage = metaDataStorage;
    }

    @Override
    public void run(ApplicationArguments args) throws Exception {
        String sFundType = args.getNonOptionArgs().get(0);

        FundTypeEnum fundType = FundTypeEnum.valueOf(sFundType);

        LocalDate endDate = LocalDate.now();

        String[] codes = metaDataStorage.getAllMetaData()
                .stream()
                .filter(item -> item.fundType().equals(fundType.getValue()))
                .map(FundMetaData::code)
                .toArray(String[]::new);

        reportService.generateReportContext(codes, endDate)
                .forEach(this::printReport);

        System.exit(0);
    }

    private void printReport(List<AnalysisContext<FundMetaData, FundHistoricalData>> contexts) {
        CompositePrinter printer = new CompositePrinter(
                new ReportHeader(),
                new InfoTable(),
                new PerformanceChart(reportConfig)
        );

        printer.print(contexts);
        System.out.println();
    }
}
