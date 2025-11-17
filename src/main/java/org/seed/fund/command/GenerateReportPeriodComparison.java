package org.seed.fund.command;

import org.seed.fund.report.ReportGenerator;
import org.seed.fund.report.model.ReportContext;
import org.seed.fund.report.printer.CompositePrinter;
import org.seed.fund.report.printer.TablePrinter;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Arrays;
import java.util.List;

@Component
@ConditionalOnProperty(name = "task", havingValue = "GenerateReportPC")
public class GenerateReportPeriodComparison implements CommandLineRunner {
    private final ReportGenerator reportGenerator;

    public GenerateReportPeriodComparison(ReportGenerator reportGenerator) {
        this.reportGenerator = reportGenerator;
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
                new TablePrinter()
//                new SharpeMddChartPrinter()
//                new ReportSummaryPrinter(),
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
