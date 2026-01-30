package com.seed.fund.command;

import com.seed.core.printer.decision.CompositePrinter;
import com.seed.core.printer.decision.DecisionTable;
import com.seed.core.printer.decision.ReportHeader;
import com.seed.core.report.BatchReportContext;
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

@Component
@ConditionalOnProperty(name = "task", havingValue = "DecisionReportByFundType")
public class DecisionReportByFundType implements ApplicationRunner {
    private final ReportService<FundMetaData, FundHistoricalData> reportService;
    private final MetaDataStorage<FundMetaData> metaDataStorage;

    public DecisionReportByFundType(ReportService<FundMetaData, FundHistoricalData> reportService,
                                    MetaDataStorage<FundMetaData> metaDataStorage) {
        this.reportService = reportService;
        this.metaDataStorage = metaDataStorage;
    }

    @Override
    public void run(ApplicationArguments args) throws Exception {
        String sFundType = args.getNonOptionArgs().get(0);
        FundTypeEnum fundType = FundTypeEnum.valueOf(sFundType);

        String[] codes = metaDataStorage.getAllMetaData()
                .stream()
                .filter(item -> item.fundType().equals(fundType.getValue()))
                .map(FundMetaData::code)
                .toArray(String[]::new);

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
