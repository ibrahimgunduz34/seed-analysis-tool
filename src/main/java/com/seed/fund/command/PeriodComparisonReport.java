package com.seed.fund.command;

import com.seed.configuration.ReportConfiguration;
import com.seed.core.AnalysisContext;
import com.seed.core.printer.snapshot.CompositePrinter;
import com.seed.core.printer.snapshot.InfoTable;
import com.seed.core.printer.snapshot.PerformanceChart;
import com.seed.core.printer.snapshot.ReportHeader;
import com.seed.core.report.ReportService;
import com.seed.fund.model.FundHistoricalData;
import com.seed.fund.model.FundMetaData;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.util.List;

@Component
@ConditionalOnProperty(name = "task", havingValue = "PeriodComparisonReport")
public class PeriodComparisonReport implements ApplicationRunner {
    private final ReportService<FundMetaData, FundHistoricalData> reportService;
    private final ReportConfiguration reportConfig;

    public PeriodComparisonReport(ReportService<FundMetaData, FundHistoricalData> reportService,
                                  ReportConfiguration reportConfig) {
        this.reportService = reportService;
        this.reportConfig = reportConfig;
    }

    @Override
    public void run(ApplicationArguments args) throws Exception {
        String[] codes = args.getNonOptionArgs().get(0).split(",");

        LocalDate endDate = LocalDate.now();

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
