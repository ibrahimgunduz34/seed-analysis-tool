package org.seed.fund.command;

import org.seed.config.FundPerformanceWeightConfig;
import org.seed.fund.report.ReportGenerator;
import org.seed.fund.report.model.ReportContext;
import org.seed.fund.report.printer.*;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Arrays;
import java.util.List;

@Component
@ConditionalOnProperty(name = "task", havingValue = "GenerateReportPerformance")
public class GenerateReportPerformanceComparison implements CommandLineRunner {
    private final ReportGenerator reportGenerator;
    private final FundPerformanceWeightConfig weightConfig;

    public GenerateReportPerformanceComparison(ReportGenerator reportGenerator, FundPerformanceWeightConfig weightConfig) {
        this.reportGenerator = reportGenerator;
        this.weightConfig = weightConfig;
    }

    @Override
    public void run(String... args) throws Exception {
        LocalDate endDate = LocalDate.now();

        List<String> codes = Arrays.stream(args[0].split(","))
                .map(String::trim).filter(s -> !s.isEmpty()).toList();

        generateReport(endDate.minusMonths(3), endDate, codes);
        generateReport(endDate.minusMonths(6), endDate, codes);
        generateReport(endDate.minusMonths(12), endDate, codes);

        System.exit(0);
    }

    private void generateReport(LocalDate beginDate, LocalDate endDate, List<String> codes) {
        CompositePrinter printer = new CompositePrinter(
                new TablePrinter(),
                new SharpeMddChartPrinter(weightConfig),
                new ReportSummaryPrinter()
//                new CommentsPrinter()
        );

        BigDecimal initialAmount = BigDecimal.valueOf(10_000);
        Integer frequency = 30;

        List<ReportContext> contexts = reportGenerator.generate(codes, beginDate, endDate, initialAmount, frequency);

        System.out.printf("Start date: %s%n", beginDate);
        System.out.printf("End date: %s%n", endDate);
        System.out.printf("Report time: %s%n", LocalDate.now());

        System.out.print("\n");

        printer.apply(contexts);
    }
}
