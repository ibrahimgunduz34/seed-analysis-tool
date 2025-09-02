package org.seed.util;

import org.seed.fund.report.model.ReportContext;

import java.util.List;

public class TablePrinter {
    private static final String[] headers = {
            "Fon",
            "Positive Days",
            "Negative Days",
            "Average Gain (%)",
            "Average Loss (%)",
            "Daily stdev (%)",
            "Periodic stdev (%)",
            "Price Change (%)",
            "Max Drawdown (%)",
            "Sharpe Ratio"
    };

    public static void print(List<ReportContext> contexts) {
        printHeaders();
        printRows(contexts);
        System.out.println("\n");
        printComments(contexts);
        System.out.println("\n");
        printNotes();

    }

    private static void printHeaders() {
        // Print table header
        for (String header : headers) {
            System.out.printf("%-22s", header);
        }
        System.out.println();
        System.out.println("-".repeat(22 * headers.length));
    }

    private static void printRows(List<ReportContext> contexts) {
        for (ReportContext ctx : contexts) {
            System.out.printf(
                    "%-22s".repeat(headers.length).concat("%n"),
                    ctx.getMetaData().getCode(),
                    "%d %.2f%%".formatted(ctx.getPositiveIncome(), ctx.getPositiveIncomePerformance()),
                    "%d %.2f%%".formatted(ctx.getNegativeIncome(), ctx.getNegativeIncomePerformance()),
                    "%.2f".formatted(BigDecimalMath.convertToPercentage(ctx.getAverageGain())),
                    "%.2f".formatted(BigDecimalMath.convertToPercentage(ctx.getAverageLoss())),
                    "%.2f".formatted(BigDecimalMath.convertToPercentage(ctx.getDailyStandardDeviation())),
                    "%.2f".formatted(BigDecimalMath.convertToPercentage(ctx.getStandardDeviation())),
                    "%.2f".formatted(BigDecimalMath.convertToPercentage(ctx.getPriceChange())),
                    "%.2f".formatted(BigDecimalMath.convertToPercentage(ctx.getMaxDrawdown())),
                    "%.2f".formatted(ctx.getSharpeRatio())
                    );
        }
    }

    private static void printNotes() {
        System.out.println("Notlar");
        System.out.println("-".repeat(25));

        System.out.println("Sharpe Ratio su sekilde degerlendirilebilir");

        System.out.println("* >2.0 → Çok iyi risk-getiri dengesi\n" +
                "* 1.0–2.0 → Kabul edilebilir, yatırım yapılabilir\n" +
                "* 0–1.0 → Getiri var ama riskine göre düşük\n" +
                "* <0 → Riskten arındırıldığında zarar");
    }

    private static void printComments(List<ReportContext> contexts) {
        contexts.forEach(context -> {
            System.out.printf("%s%n", context.getEvaluationSummary());
        });
    }
}
