package com.seed.fund.command;

import com.seed.core.printer.decision.CompositePrinter;
import com.seed.core.printer.decision.DecisionTable;
import com.seed.core.printer.decision.ReportHeader;
import com.seed.core.report.BatchReportContext;
import com.seed.core.report.ReportService;
import com.seed.fund.model.FundHistoricalData;
import com.seed.fund.model.FundMetaData;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Component;

import java.time.LocalDate;

@Component
@ConditionalOnProperty(name = "task", havingValue = "DecisionReport")
public class DecisionReport implements ApplicationRunner {
    private final ReportService<FundMetaData, FundHistoricalData> reportService;

    public DecisionReport(ReportService<FundMetaData, FundHistoricalData> reportService) {
        this.reportService = reportService;
    }

    @Override
    public void run(ApplicationArguments args) throws Exception {
        String[] codes = args.getNonOptionArgs().get(0).split(",");

        LocalDate endDate = LocalDate.now();

        BatchReportContext batchReportContext = reportService.generateDecisionReportContext(codes, endDate);
        printReport(batchReportContext);

        System.exit(0);
    }

    public void printReport(BatchReportContext reportContext) {
        CompositePrinter printer = new CompositePrinter(
                new ReportHeader(),
                new DecisionTable()
        );

        printer.print(reportContext);
    }
}
