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

        double minSharpe = contexts.stream().mapToDouble(c -> safeDouble(c.getSharpeRatio())).min().orElse(0.0);
        double maxSharpe = contexts.stream().mapToDouble(c -> safeDouble(c.getSharpeRatio())).max().orElse(1.0);
        double minMDD = contexts.stream().mapToDouble(c -> Math.abs(safeDouble(c.getMaxDrawdown()))).min().orElse(0.0);
        double maxMDD = contexts.stream().mapToDouble(c -> Math.abs(safeDouble(c.getMaxDrawdown()))).max().orElse(1.0);

        System.out.println("Fon Performans & Risk Karşılaştırması");
        System.out.printf("%s%n", "-".repeat(50));

        List<ReportContext> sortedContexts = new java.util.ArrayList<>(contexts);

        sortedContexts.sort((a, b) -> {
            double sharpeA = safeDouble(a.getSharpeRatio());
            double mddA = Math.abs(safeDouble(a.getMaxDrawdown()));
            double sharpeB = safeDouble(b.getSharpeRatio());
            double mddB = Math.abs(safeDouble(b.getMaxDrawdown()));

            double sharpeNormA = (sharpeA - minSharpe) / (maxSharpe - minSharpe + 1e-6);
            double mddNormA = 1.0 - (mddA - minMDD) / (maxMDD - minMDD + 1e-6);
            double combinedA = (sharpeNormA + mddNormA) / 2.0;

            double sharpeNormB = (sharpeB - minSharpe) / (maxSharpe - minSharpe + 1e-6);
            double mddNormB = 1.0 - (mddB - minMDD) / (maxMDD - minMDD + 1e-6);
            double combinedB = (sharpeNormB + mddNormB) / 2.0;

            return Double.compare(combinedB, combinedA); // descending order
        });

        for (ReportContext ctx : sortedContexts) {
            String code = ctx.getMetaData().getCode();

            double sharpe = safeDouble(ctx.getSharpeRatio());
            double mdd = Math.abs(safeDouble(ctx.getMaxDrawdown()));

            double sharpeNorm = (sharpe - minSharpe) / (maxSharpe - minSharpe + 1e-6);
            double mddNorm = 1.0 - (mdd - minMDD) / (maxMDD - minMDD + 1e-6);
            double combinedScore = (sharpeNorm + mddNorm) / 2.0;

            int barLength = (int) Math.round(combinedScore * maxBarLength);
            barLength = Math.max(barLength, 1);

            String bar = "█".repeat(barLength) +
                    "░".repeat(Math.max(0, maxBarLength - barLength));

            System.out.printf("%-6s | %s (Sharpe: %.2f, MDD: -%.2f%%)%n",
                    code, bar, sharpe, mdd * 100);
        }
    }


    private  double safeDouble(BigDecimal value) {
        return value != null ? value.doubleValue() : 0.0;
    }
}
