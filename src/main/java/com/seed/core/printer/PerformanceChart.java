package com.seed.core.printer;

import com.seed.configuration.ReportConfiguration;
import com.seed.core.AnalysisContext;
import com.seed.core.model.HistoricalData;
import com.seed.core.model.MetaData;

import java.math.BigDecimal;
import java.util.List;

public class PerformanceChart implements Printer {
    private final ReportConfiguration reportConfiguration;

    public PerformanceChart(ReportConfiguration reportConfiguration) {
        this.reportConfiguration = reportConfiguration;
    }

    @Override
    public <M extends MetaData, H extends HistoricalData> void print(List<AnalysisContext<M, H>> analysisContexts) {
        printChart(analysisContexts);
        System.out.println("\n");
    }

    private <M extends MetaData, H extends HistoricalData> void printChart(List<AnalysisContext<M, H>> contexts) {
        if (contexts == null || contexts.isEmpty()) {
            System.out.println("Hiç fon verisi yok.");
            return;
        }

        int maxBarLength = 20; // çubuğun maksimum uzunluğu

        // Normalizasyon için min/max değerleri bul
        double minSharpe = contexts.stream().mapToDouble(c -> safeDouble(c.getStatistics().sharpeRatio())).min().orElse(0.0);
        double maxSharpe = contexts.stream().mapToDouble(c -> safeDouble(c.getStatistics().sharpeRatio())).max().orElse(1.0);

        double minMDD = contexts.stream().mapToDouble(c -> Math.abs(safeDouble(c.getStatistics().mdd()))).min().orElse(0.0);
        double maxMDD = contexts.stream().mapToDouble(c -> Math.abs(safeDouble(c.getStatistics().mdd()))).max().orElse(1.0);

        double minReturn = contexts.stream().mapToDouble(c -> safeDouble(c.getStatistics().priceChange())).min().orElse(0.0);
        double maxReturn = contexts.stream().mapToDouble(c -> safeDouble(c.getStatistics().priceChange())).max().orElse(1.0);

        System.out.println("Fon Performans & Risk Karşılaştırması");
        System.out.printf("%s%n", "-".repeat(50));

        List<AnalysisContext<M, H>> sortedContexts = new java.util.ArrayList<>(contexts);

        // Sharpe + MDD + Getiri → ağırlıklı ortalama skor
        sortedContexts.sort((a, b) -> {
            double scoreA = calculateCombinedScore(a, minSharpe, maxSharpe, minMDD, maxMDD, minReturn, maxReturn);
            double scoreB = calculateCombinedScore(b, minSharpe, maxSharpe, minMDD, maxMDD, minReturn, maxReturn);
            return Double.compare(scoreB, scoreA); // descending
        });

        for (AnalysisContext<M, H> ctx : sortedContexts) {
            String code = ctx.getMetaData().code();

            double sharpe = safeDouble(ctx.getStatistics().sharpeRatio());
            double mdd = Math.abs(safeDouble(ctx.getStatistics().mdd()));
            double returnPct = safeDouble(ctx.getStatistics().priceChange());

            double combinedScore = calculateCombinedScore(ctx, minSharpe, maxSharpe, minMDD, maxMDD, minReturn, maxReturn);
            int barLength = (int) Math.round(combinedScore * maxBarLength);
            barLength = Math.max(barLength, 1);

            String bar = "█".repeat(barLength) +
                    "░".repeat(Math.max(0, maxBarLength - barLength));

            System.out.printf("%-6s | %s (Sharpe: %.2f, MDD: -%.2f%%, Return: %.2f%%)%n",
                    code, bar, sharpe, mdd * 100, returnPct * 100);
        }
    }

    private <M extends MetaData, H extends HistoricalData> double calculateCombinedScore(AnalysisContext<M, H> ctx,
                                          double minSharpe, double maxSharpe,
                                          double minMDD, double maxMDD,
                                          double minReturn, double maxReturn) {
        double sharpe = safeDouble(ctx.getStatistics().sharpeRatio());
        double mdd = Math.abs(safeDouble(ctx.getStatistics().mdd()));
        double returnPct = safeDouble(ctx.getStatistics().priceChange());

        double sharpeNorm = (sharpe - minSharpe) / (maxSharpe - minSharpe + 1e-6);
        double mddNorm = 1.0 - (mdd - minMDD) / (maxMDD - minMDD + 1e-6); // düşük MDD daha iyi
        double returnNorm = (returnPct - minReturn) / (maxReturn - minReturn + 1e-6);

        // Ağırlıklar: Sharpe %40, MDD %40, Getiri %20
        return reportConfiguration.sharpe() * sharpeNorm + reportConfiguration.mdd() * mddNorm + reportConfiguration.returnPct() * returnNorm;
    }

    private double safeDouble(BigDecimal value) {
        return value != null ? value.doubleValue() : 0.0;
    }
}
