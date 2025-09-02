package org.seed.fund.command;

import org.seed.fund.report.ReportGenerator;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Stream;

@Component
@ConditionalOnProperty(name = "task", havingValue = "GenerateReport")
public class GenerateReport implements CommandLineRunner {
    private final ReportGenerator reportGenerator;

    public GenerateReport(ReportGenerator reportGenerator) {
        this.reportGenerator = reportGenerator;
    }

    @Override
    public void run(String... args) throws Exception {
//        List<String> codes = List.of(
//                "BDS",
//                "PPH",
//                "DTL",
//                "KHC",
//                "KVT",
//                "GNS"
//        );

//        LocalDate beginDate = LocalDate.parse("2025-02-29", DateTimeFormatter.ofPattern("yyyy-MM-dd"));
//        LocalDate endDate = LocalDate.parse("2025-09-02", DateTimeFormatter.ofPattern("yyyy-MM-dd"));

        LocalDate beginDate = LocalDate.parse(args[0], DateTimeFormatter.ofPattern("yyyy-MM-dd"));
        LocalDate endDate = LocalDate.parse(args[1], DateTimeFormatter.ofPattern("yyyy-MM-dd"));

        List<String> codes = Arrays.stream(args[2].split(","))
                .map(String::trim).filter(s -> !s.isEmpty()).toList();

        BigDecimal initialAmount = BigDecimal.valueOf(10_000);
        Integer frequency = 30;

        reportGenerator.generate(codes, beginDate, endDate, initialAmount, frequency);
    }
}
