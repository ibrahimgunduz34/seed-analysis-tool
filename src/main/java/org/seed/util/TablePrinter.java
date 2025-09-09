package org.seed.util;

import org.seed.fund.report.model.ReportContext;

import java.math.BigDecimal;
import java.util.List;

public class TablePrinter {
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

    public static void print(List<ReportContext> contexts) {
        printHeaders();
        printRows(contexts);
        System.out.println("\n");
        printComments(contexts);
        System.out.println("\n");
        printSharpeMDDChart(contexts);
    }

    private static void printHeaders() {
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

    private static void printSharpeMDDChart(List<ReportContext> contexts) {
        if (contexts == null || contexts.isEmpty()) {
            System.out.println("Hiç fon verisi yok.");
            return;
        }

        int maxBarLength = 20; // çubuğun maksimum uzunluğu

        double minSharpe = contexts.stream().mapToDouble(c -> safeDouble(c.getSharpeRatio())).min().orElse(0.0);
        double maxSharpe = contexts.stream().mapToDouble(c -> safeDouble(c.getSharpeRatio())).max().orElse(1.0);
        double minMDD = contexts.stream().mapToDouble(c -> Math.abs(safeDouble(c.getMaxDrawdown()))).min().orElse(0.0);
        double maxMDD = contexts.stream().mapToDouble(c -> Math.abs(safeDouble(c.getMaxDrawdown()))).max().orElse(1.0);

        System.out.println("Fon Performans & Risk Karşılaştırması");
        System.out.println("-----------------------------");

        for (ReportContext ctx : contexts) {
            String code = ctx.getFundMetaData().getCode();

            double sharpe = safeDouble(ctx.getSharpeRatio());
            double mdd = Math.abs(safeDouble(ctx.getMaxDrawdown()));

            // Sharpe ve MDD’den global normalize edilmiş skor
            double sharpeNorm = (sharpe - minSharpe) / (maxSharpe - minSharpe + 1e-6);
            double mddNorm = 1.0 - (mdd - minMDD) / (maxMDD - minMDD + 1e-6); // düşük MDD → yüksek değer

            double combinedScore = (sharpeNorm + mddNorm) / 2.0; // basit ortalama
            int barLength = (int) Math.round(combinedScore * maxBarLength);
            barLength = Math.max(barLength, 1);

            StringBuilder bar = new StringBuilder();
            for (int i = 0; i < barLength; i++) bar.append("█");
            for (int i = barLength; i < maxBarLength; i++) bar.append("░");

            System.out.printf("%-6s | %s (Sharpe: %.2f, MDD: -%.2f%%)%n",
                    code, bar.toString(), sharpe, mdd * 100);
        }
    }

    private static double safeDouble(BigDecimal value) {
        return value != null ? value.doubleValue() : 0.0;
    }

    private static void printComments(List<ReportContext> contexts) {
        contexts.forEach(context -> {
            System.out.printf("%s%n", context.getEvaluationSummary());
        });
    }
}
