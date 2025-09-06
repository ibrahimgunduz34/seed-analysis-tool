package org.seed.fund.report.printer;

import org.seed.fund.report.calculator.SummaryReportCalculator;
import org.seed.fund.report.model.ReportContext;

import java.util.List;
import java.util.function.Function;

public class ReportSummaryPrinter implements Function<List<ReportContext>, List<ReportContext>> {
    @Override
    public List<ReportContext> apply(List<ReportContext> contexts) {
        System.out.printf("Ozet:%n%s%n", "-".repeat(50));

        SummaryReportCalculator calculator = new SummaryReportCalculator();
        String summary = calculator.apply(contexts);

        System.out.println(summary);
        System.out.println("\n");

        return contexts;
    }
}
