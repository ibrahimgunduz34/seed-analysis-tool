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

        for (ReportContext ctx : contexts) {
            String code = ctx.getMetaData().getCode();

            double sharpe = safeDouble(ctx.getSharpeRatio());
            double mdd = Math.abs(safeDouble(ctx.getMaxDrawdown()));

            // Sharpe ve MDD’den global normalize edilmiş skor
            double sharpeNorm = (sharpe - minSharpe) / (maxSharpe - minSharpe + 1e-6);
            double mddNorm = 1.0 - (mdd - minMDD) / (maxMDD - minMDD + 1e-6); // düşük MDD → yüksek değer

            double combinedScore = (sharpeNorm + mddNorm) / 2.0; // basit ortalama
            int barLength = (int) Math.round(combinedScore * maxBarLength);
            barLength = Math.max(barLength, 1);

            StringBuilder bar = new StringBuilder();
            bar.append("█".repeat(barLength));
            bar.append("░".repeat(Math.max(0, maxBarLength - barLength)));

            System.out.printf("%-6s | %s (Sharpe: %.2f, MDD: -%.2f%%)%n",
                    code, bar.toString(), sharpe, mdd * 100);
        }
    }

    private  double safeDouble(BigDecimal value) {
        return value != null ? value.doubleValue() : 0.0;
    }
}
