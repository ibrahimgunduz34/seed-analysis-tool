package org.seed.fund.command;

import org.seed.config.FundPerformanceWeightConfig;
import org.seed.fund.model.FundMetaData;
import org.seed.fund.report.ReportGenerator;
import org.seed.fund.report.model.ReportContext;
import org.seed.fund.report.printer.*;
import org.seed.fund.storage.FundStorage;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Component
@ConditionalOnProperty(name = "task", havingValue = "GenerateReportAll")
public class GenerateReportAll implements CommandLineRunner {
    private final ReportGenerator reportGenerator;
    private final FundStorage fundStorage;
    private final FundPerformanceWeightConfig weightConfig;


    public GenerateReportAll(ReportGenerator reportGenerator, FundStorage fundStorage, FundPerformanceWeightConfig weightConfig) {
        this.reportGenerator = reportGenerator;
        this.fundStorage = fundStorage;
        this.weightConfig = weightConfig;
    }

    @Override
    public void run(String... args) throws Exception {
        LocalDate beginDate = LocalDate.parse(args[0], DateTimeFormatter.ofPattern("yyyy-MM-dd"));
        LocalDate endDate = LocalDate.parse(args[1], DateTimeFormatter.ofPattern("yyyy-MM-dd"));

        BigDecimal initialAmount = BigDecimal.valueOf(10_000);
        Integer frequency = 30;

        CompositePrinter printer = new CompositePrinter(
                new SharpeMddChartPrinter(weightConfig),
                new ReportSummaryPrinter()
        );

        Map<String, List<FundMetaData>> groupedMetaDataList = fundStorage.getMetaDataList().stream()
                .collect(Collectors.groupingBy(FundMetaData::getFundType));

        System.out.printf("Start date: %s%n", beginDate);
        System.out.printf("End date: %s%n", endDate);
        System.out.printf("Report time: %s%n", LocalDate.now());

        System.out.print("\n");

        groupedMetaDataList.forEach((key, metaDataList) -> {
            System.out.println(key);
            System.out.printf("%s%n", "=".repeat(50));

            List<String> codes = metaDataList.stream().map(FundMetaData::getCode).toList();
            List<ReportContext> contexts = reportGenerator.generate(codes, beginDate, endDate, initialAmount, frequency);

            printer.apply(contexts);

            System.out.println("\n");

            System.exit(0);
        });
    }
}
