package org.seed.fund.report.printer;

import org.seed.fund.report.model.ReportContext;

import java.math.BigDecimal;
import java.util.List;
import java.util.function.Function;

public class SharpeMddChartPrinter implements Function<List<ReportContext>, List<ReportContext>> {
    @Override
    public List<ReportContext> apply(List<ReportContext> contexts) {
        printChart(contexts);
        System.out.println("\n");
        return contexts;
    }

    private void printChart(List<ReportContext> contexts) {
        if (contexts == null || contexts.isEmpty()) {
            System.out.println("Hiç fon verisi yok.");
            return;
        }

        int maxBarLength = 20; // çubuğun maksimum uzunluğu

        // Normalizasyon için min/max değerleri bul
        double minSharpe = contexts.stream().mapToDouble(c -> safeDouble(c.getSharpeRatio())).min().orElse(0.0);
        double maxSharpe = contexts.stream().mapToDouble(c -> safeDouble(c.getSharpeRatio())).max().orElse(1.0);

        double minMDD = contexts.stream().mapToDouble(c -> Math.abs(safeDouble(c.getMaxDrawdown()))).min().orElse(0.0);
        double maxMDD = contexts.stream().mapToDouble(c -> Math.abs(safeDouble(c.getMaxDrawdown()))).max().orElse(1.0);

        double minReturn = contexts.stream().mapToDouble(c -> safeDouble(c.getPriceChange())).min().orElse(0.0);
        double maxReturn = contexts.stream().mapToDouble(c -> safeDouble(c.getPriceChange())).max().orElse(1.0);

        System.out.println("Fon Performans & Risk Karşılaştırması");
        System.out.printf("%s%n", "-".repeat(50));

        List<ReportContext> sortedContexts = new java.util.ArrayList<>(contexts);

        // Sharpe + MDD + Getiri → ağırlıklı ortalama skor
        sortedContexts.sort((a, b) -> {
            double scoreA = calculateCombinedScore(a, minSharpe, maxSharpe, minMDD, maxMDD, minReturn, maxReturn);
            double scoreB = calculateCombinedScore(b, minSharpe, maxSharpe, minMDD, maxMDD, minReturn, maxReturn);
            return Double.compare(scoreB, scoreA); // descending
        });

        for (ReportContext ctx : sortedContexts) {
            String code = ctx.getFundMetaData().getCode();

            double sharpe = safeDouble(ctx.getSharpeRatio());
            double mdd = Math.abs(safeDouble(ctx.getMaxDrawdown()));
            double returnPct = safeDouble(ctx.getPriceChange());

            double combinedScore = calculateCombinedScore(ctx, minSharpe, maxSharpe, minMDD, maxMDD, minReturn, maxReturn);
            int barLength = (int) Math.round(combinedScore * maxBarLength);
            barLength = Math.max(barLength, 1);

            String bar = "█".repeat(barLength) +
                    "░".repeat(Math.max(0, maxBarLength - barLength));

            System.out.printf("%-6s | %s (Sharpe: %.2f, MDD: -%.2f%%, Return: %.2f%%)%n",
                    code, bar, sharpe, mdd * 100, returnPct * 100);
        }
    }

    private double calculateCombinedScore(ReportContext ctx,
                                          double minSharpe, double maxSharpe,
                                          double minMDD, double maxMDD,
                                          double minReturn, double maxReturn) {
        double sharpe = safeDouble(ctx.getSharpeRatio());
        double mdd = Math.abs(safeDouble(ctx.getMaxDrawdown()));
        double returnPct = safeDouble(ctx.getPriceChange());

        double sharpeNorm = (sharpe - minSharpe) / (maxSharpe - minSharpe + 1e-6);
        double mddNorm = 1.0 - (mdd - minMDD) / (maxMDD - minMDD + 1e-6); // düşük MDD daha iyi
        double returnNorm = (returnPct - minReturn) / (maxReturn - minReturn + 1e-6);

        // Ağırlıklar: Sharpe %40, MDD %40, Getiri %20
        return 0.4 * sharpeNorm + 0.4 * mddNorm + 0.2 * returnNorm;
    }

    private double safeDouble(BigDecimal value) {
        return value != null ? value.doubleValue() : 0.0;
    }
}
