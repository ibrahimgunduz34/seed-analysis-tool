package org.seed.fund.report.calculator;

import org.seed.fund.report.model.ReportContext;

import java.util.Comparator;
import java.util.List;
import java.util.function.Function;
import java.util.stream.Collectors;

public class SummaryReportCalculator implements Function<List<ReportContext>, String> {

    @Override
    public String apply(List<ReportContext> ctxList) {
        if (ctxList == null || ctxList.isEmpty()) {
            return "Hiç fon verisi yok.";
        }

        StringBuilder summary = new StringBuilder();
        summary.append("=== Özet Rapor ===\n\n");

        // 1. En yüksek Sharpe Ratio
        ReportContext bestSharpe = ctxList.stream()
                .max(Comparator.comparing(c -> c.getSharpeRatio() != null ? c.getSharpeRatio().doubleValue() : 0.0))
                .orElse(null);
        summary.append(String.format("En iyi risk-düzeltilmiş getiri (Sharpe): %s %.2f%n",
                bestSharpe.getMetaData().getCode(),
                bestSharpe.getSharpeRatio().doubleValue()));

        // 2. En yüksek fiyat değişimi
        ReportContext bestPriceChange = ctxList.stream()
                .max(Comparator.comparing(c -> c.getPriceChange() != null ? c.getPriceChange().doubleValue() : Double.NEGATIVE_INFINITY))
                .orElse(null);
        summary.append(String.format("En yüksek fiyat değişimi: %s %.2f%%%n",
                bestPriceChange.getMetaData().getCode(),
                bestPriceChange.getPriceChange().doubleValue() * 100));

        // 3. En düşük fiyat değişimi
        ReportContext worstPriceChange = ctxList.stream()
                .min(Comparator.comparing(c -> c.getPriceChange() != null ? c.getPriceChange().doubleValue() : Double.POSITIVE_INFINITY))
                .orElse(null);
        summary.append(String.format("En düşük fiyat değişimi: %s %.2f%%%n",
                worstPriceChange.getMetaData().getCode(),
                worstPriceChange.getPriceChange().doubleValue() * 100));

        // 4. En yüksek ve en düşük risk (Max Drawdown bazlı)
        ReportContext highestRisk = ctxList.stream()
                .max(Comparator.comparing(c -> c.getMaxDrawdown() != null ? Math.abs(c.getMaxDrawdown().doubleValue()) : 0.0))
                .orElse(null);
        ReportContext lowestRisk = ctxList.stream()
                .min(Comparator.comparing(c -> c.getMaxDrawdown() != null ? Math.abs(c.getMaxDrawdown().doubleValue()) : Double.MAX_VALUE))
                .orElse(null);
        summary.append(String.format("En yüksek risk (MDD): %s %.2f%%%n",
                highestRisk.getMetaData().getCode(),
                highestRisk.getMaxDrawdown().doubleValue() * 100));
        summary.append(String.format("En düşük risk (MDD): %s %.2f%%%n",
                lowestRisk.getMetaData().getCode(),
                lowestRisk.getMaxDrawdown().doubleValue() * 100));

        // 5. Ortalama volatilite karşılaştırması
        double avgVolatility = ctxList.stream()
                .mapToDouble(c -> c.getStandardDeviation() != null ? c.getStandardDeviation().doubleValue() * 100 : 0.0)
                .average().orElse(0.0);
        summary.append(String.format("Tüm fonların ortalama volatilitesi: %.2f%%%n", avgVolatility));

        // 6. Fonları Sharpe / MDD oranına göre sıralayıp ilk 3
        List<ReportContext> top3BySharpeToRisk = ctxList.stream()
                .sorted(Comparator.comparingDouble(c -> {
                    double mdd = c.getMaxDrawdown() != null ? Math.abs(c.getMaxDrawdown().doubleValue()) : 1.0;
                    double sharpe = c.getSharpeRatio() != null ? c.getSharpeRatio().doubleValue() : 0.0;
                    return -sharpe / (mdd + 1e-6); // büyükten küçüğe
                }))
                .limit(3)
                .toList();

        summary.append("En iyi Sharpe/MDD dengesi olan ilk 3 fon:\n");
        for (ReportContext c : top3BySharpeToRisk) {
            summary.append(String.format(" - %s (Sharpe: %.2f, MDD: %.2f%%)%n",
                    c.getMetaData().getCode(),
                    c.getSharpeRatio().doubleValue(),
                    c.getMaxDrawdown().doubleValue() * 100));
        }

        return summary.toString();
    }
}
