package org.seed.fund.report.printer;

import org.seed.fund.report.model.ReportContext;
import org.seed.util.BigDecimalMath;

import java.util.List;
import java.util.function.Function;

public class TablePrinter implements Function<List<ReportContext>, List<ReportContext>> {
    private static final String[] headers = {
            "Fon",
            "Positive Days",
            "Negative Days",
            "Average Gain (%)",
            "Average Loss (%)",
            "Stdev (%)",
            "Price Change (%)",
            "Max Drawdown (%)",
            "Sortino Ratio",
            "Sharpe Ratio"
    };

    @Override
    public List<ReportContext> apply(List<ReportContext> contexts) {
        printHeaders();
        printRows(contexts);

        System.out.println("\n");

        return contexts;
    }

    private void printHeaders() {
        for (String header : headers) {
            System.out.printf("%-22s", header);
        }
        System.out.println();
        System.out.println("-".repeat(22 * headers.length));
    }

    private void printRows(List<ReportContext> contexts) {
        for (ReportContext ctx : contexts) {
            System.out.printf(
                    "%-22s".repeat(headers.length).concat("%n"),
                    ctx.getFundMetaData().getCode(),
                    "%d %.2f%%".formatted(ctx.getNumberOfPositiveDays(), ctx.getWeightOfPositiveDays()),
                    "%d %.2f%%".formatted(ctx.getNumberOfNegativeDays(), ctx.getWeightOfNegativeDays()),
                    "%.2f".formatted(BigDecimalMath.convertToPercentage(ctx.getAverageGain())),
                    "%.2f".formatted(BigDecimalMath.convertToPercentage(ctx.getAverageLoss())),
                    "%.2f".formatted(BigDecimalMath.convertToPercentage(ctx.getStandardDeviation())),
                    "%.2f".formatted(BigDecimalMath.convertToPercentage(ctx.getPriceChange())),
                    "%.2f".formatted(BigDecimalMath.convertToPercentage(ctx.getMaxDrawdown())),
                    "%.2f".formatted(ctx.getSortinoRatio()),
                    "%.2f".formatted(ctx.getSharpeRatio())
            );
        }
    }
}
