package org.seed.fund.command;

import org.seed.fund.report.ReportGenerator;
import org.seed.fund.report.model.ReportContext;
import org.seed.fund.report.printer.*;
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
        LocalDate beginDate = LocalDate.parse(args[0], DateTimeFormatter.ofPattern("yyyy-MM-dd"));
        LocalDate endDate = LocalDate.parse(args[1], DateTimeFormatter.ofPattern("yyyy-MM-dd"));

        List<String> codes = Arrays.stream(args[2].split(","))
                .map(String::trim).filter(s -> !s.isEmpty()).toList();

        BigDecimal initialAmount = BigDecimal.valueOf(10_000);
        Integer frequency = 30;

        CompositePrinter printer = new CompositePrinter(
                new TablePrinter(),
                new SharpeMddChartPrinter(),
                new ReportSummaryPrinter(),
                new CommentsPrinter()
        );

        List<ReportContext> contexts = reportGenerator.generate(codes, beginDate, endDate, initialAmount, frequency);

        System.out.printf("Start date: %s%n", beginDate);
        System.out.printf("End date: %s%n", endDate);
        System.out.printf("Report time: %s%n", LocalDate.now());

        System.out.print("\n");

        printer.apply(contexts);
    }
}
