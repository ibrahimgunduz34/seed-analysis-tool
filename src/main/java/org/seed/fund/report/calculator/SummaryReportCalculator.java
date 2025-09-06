package org.seed.fund.report.calculator;

import org.seed.fund.report.model.ReportContext;

import java.math.BigDecimal;
import java.util.Comparator;
import java.util.List;
import java.util.function.Function;

public class SummaryReportCalculator implements Function<List<ReportContext>, String> {

    @Override
    public String apply(List<ReportContext> ctxList) {
        if (ctxList == null || ctxList.isEmpty()) return "Hiç fon verisi yok.";

        StringBuilder summary = new StringBuilder();
        summary.append("=== Özet Rapor ===\n\n");

        // En iyi / kötü bulmak için karşılaştırmalar
        ReportContext bestSharpe = ctxList.stream()
                .max(Comparator.comparing(c -> safeDouble(c.getSharpeRatio())))
                .orElse(null);
        ReportContext bestPriceChange = ctxList.stream()
                .max(Comparator.comparing(c -> safeDouble(c.getPriceChange())))
                .orElse(null);
        ReportContext worstPriceChange = ctxList.stream()
                .min(Comparator.comparing(c -> safeDouble(c.getPriceChange())))
                .orElse(null);
        ReportContext highestRisk = ctxList.stream()
                .max(Comparator.comparing(c -> Math.abs(safeDouble(c.getMaxDrawdown()))))
                .orElse(null);
        ReportContext lowestRisk = ctxList.stream()
                .min(Comparator.comparing(c -> Math.abs(safeDouble(c.getMaxDrawdown()))))
                .orElse(null);

        summary.append(String.format("En iyi risk-düzeltilmiş getiri (Sharpe): %s %.2f%n",
                bestSharpe.getMetaData().getCode(),
                safeDouble(bestSharpe.getSharpeRatio())));
        summary.append(String.format("En yüksek fiyat değişimi: %s %.2f%%%n",
                bestPriceChange.getMetaData().getCode(),
                safeDouble(bestPriceChange.getPriceChange()) * 100));
        summary.append(String.format("En düşük fiyat değişimi: %s %.2f%%%n",
                worstPriceChange.getMetaData().getCode(),
                safeDouble(worstPriceChange.getPriceChange()) * 100));
        summary.append(String.format("En yüksek risk (MDD): %s %.2f%%%n",
                highestRisk.getMetaData().getCode(),
                safeDouble(highestRisk.getMaxDrawdown()) * 100));
        summary.append(String.format("En düşük risk (MDD): %s %.2f%%%n",
                lowestRisk.getMetaData().getCode(),
                safeDouble(lowestRisk.getMaxDrawdown()) * 100));

        double avgVolatility = ctxList.stream()
                .mapToDouble(c -> safeDouble(c.getStandardDeviation()) * 100)
                .average().orElse(0.0);
        summary.append(String.format("Tüm fonların ortalama volatilitesi: %.2f%%%n", avgVolatility));

        // Min / max değerleri dinamik kıyaslama için
        double minSharpe = ctxList.stream().mapToDouble(c -> safeDouble(c.getSharpeRatio())).min().orElse(0.0);
        double maxSharpe = ctxList.stream().mapToDouble(c -> safeDouble(c.getSharpeRatio())).max().orElse(1.0);
        double minMdd = ctxList.stream().mapToDouble(c -> Math.abs(safeDouble(c.getMaxDrawdown()))).min().orElse(0.0);
        double maxMdd = ctxList.stream().mapToDouble(c -> Math.abs(safeDouble(c.getMaxDrawdown()))).max().orElse(1.0);
        double minVol = ctxList.stream().mapToDouble(c -> safeDouble(c.getStandardDeviation()) * 100).min().orElse(0.0);
        double maxVol = ctxList.stream().mapToDouble(c -> safeDouble(c.getStandardDeviation()) * 100).max().orElse(1.0);

        // Sharpe/MDD dengesine göre ilk 3 fon
        List<ReportContext> top3BySharpeToRisk = ctxList.stream()
                .sorted(Comparator.comparingDouble(
                        c -> -safeDouble(c.getSharpeRatio()) / (Math.abs(safeDouble(c.getMaxDrawdown())) + 1e-6)))
                .limit(3)
                .toList();

        summary.append("En iyi Sharpe/MDD dengesi olan ilk 3 fon:\n");
        for (ReportContext c : top3BySharpeToRisk) {
            double sharpe = safeDouble(c.getSharpeRatio());
            double mdd = safeDouble(c.getMaxDrawdown()) * 100;
            double volatility = safeDouble(c.getStandardDeviation()) * 100;

            summary.append(String.format(" - %s (Sharpe: %.2f, MDD: %.2f%%) : %s%n",
                    c.getMetaData().getCode(),
                    sharpe,
                    mdd,
                    investorGuidance(sharpe, mdd, volatility,
                            minSharpe, maxSharpe,
                            minMdd, maxMdd,
                            minVol, maxVol)));
        }

        return summary.toString();
    }

    private double safeDouble(BigDecimal value) {
        return value != null ? value.doubleValue() : 0.0;
    }

    private String investorGuidance(double sharpe, double mdd, double volatility,
                                    double minSharpe, double maxSharpe,
                                    double minMdd, double maxMdd,
                                    double minVol, double maxVol) {

        double sharpeScore = (sharpe - minSharpe) / (maxSharpe - minSharpe + 1e-6);
        double mddScore = (Math.abs(mdd) - Math.abs(minMdd)) / (Math.abs(maxMdd - minMdd) + 1e-6);
        double volScore = (volatility - minVol) / (maxVol - minVol + 1e-6);

        if (sharpeScore > 0.7 && mddScore < 0.3 && volScore < 0.3) {
            return "Düşük riskli ve yüksek getirili, dengeli portföylerde tercih edilebilir";
        } else if (sharpeScore > 0.5 && mddScore < 0.6) {
            return "Orta/uzun vadeli yatırım için uygun, volatiliteye dikkat";
        } else {
            return "Yüksek dalgalanma ve risk mevcut, yalnızca yüksek risk toleransı olan yatırımcılar için";
        }
    }
}
