package org.seed.fund.command;

import org.seed.fund.model.MetaData;
import org.seed.fund.report.ReportGenerator;
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


    public GenerateReportAll(ReportGenerator reportGenerator, FundStorage fundStorage) {
        this.reportGenerator = reportGenerator;
        this.fundStorage = fundStorage;
    }

    @Override
    public void run(String... args) throws Exception {
        LocalDate beginDate = LocalDate.parse(args[0], DateTimeFormatter.ofPattern("yyyy-MM-dd"));
        LocalDate endDate = LocalDate.parse(args[1], DateTimeFormatter.ofPattern("yyyy-MM-dd"));

        BigDecimal initialAmount = BigDecimal.valueOf(10_000);
        Integer frequency = 30;

        Map<String, List<MetaData>> groupedMetaDataList = fundStorage.getMetaDataList().stream()
                .collect(Collectors.groupingBy(MetaData::getFundType));

        groupedMetaDataList.forEach((key, metaDataList) -> {
            System.out.println(key);
            System.out.println("=".repeat(30));
            System.out.println("\n");

            List<String> codes = metaDataList.stream().map(MetaData::getCode).toList();
            reportGenerator.generate(codes, beginDate, endDate, initialAmount, frequency);
            System.out.println("\n");
        });
    }
}
