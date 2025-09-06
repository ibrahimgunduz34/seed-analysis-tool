package org.seed.fund.report.printer;

import org.seed.fund.report.calculator.FundEvaluationCalculator;
import org.seed.fund.report.model.ReportContext;

import java.util.List;
import java.util.function.Function;

public class CommentsPrinter implements Function<List<ReportContext>, List<ReportContext>> {
    @Override
    public List<ReportContext> apply(List<ReportContext> contexts) {
        FundEvaluationCalculator calculator = new FundEvaluationCalculator();
        calculator.apply(contexts);

        System.out.printf("Fon Yorumlari: %n%s%n", "-".repeat(50));

        contexts.forEach(context -> {
            System.out.printf("%s%n", context.getEvaluationSummary());
        });

        System.out.println("\n");

        return contexts;
    }
}
