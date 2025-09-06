package org.seed.fund.report.printer;

import org.seed.fund.report.model.ReportContext;

import java.util.Arrays;
import java.util.List;
import java.util.function.Function;

public class CompositePrinter implements Function<List<ReportContext>, List<ReportContext>> {

    private final Function<List<ReportContext>, List<ReportContext>> printer;

    public CompositePrinter(Function<List<ReportContext>, List<ReportContext>> ... printers) {
        this.printer = Arrays.stream(printers)
                .reduce(Function.identity(), Function::andThen);
    }

    @Override
    public List<ReportContext> apply(List<ReportContext> contexts) {
        this.printer.apply(contexts);

        return contexts;
    }
}
