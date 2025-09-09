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

        // Genel istatistikler
        double avgVolatility = ctxList.stream()
                .mapToDouble(c -> safeDouble(c.getStandardDeviation()) * 100)
                .average().orElse(0.0);

        summary.append(String.format("Tüm fonların ortalama volatilitesi: %.2f%%%n", avgVolatility));

        // Normalizasyon için min/max değerleri çıkar
        double minSharpe = ctxList.stream().mapToDouble(c -> safeDouble(c.getSharpeRatio())).min().orElse(0.0);
        double maxSharpe = ctxList.stream().mapToDouble(c -> safeDouble(c.getSharpeRatio())).max().orElse(1.0);
        double minMdd = ctxList.stream().mapToDouble(c -> Math.abs(safeDouble(c.getMaxDrawdown()))).min().orElse(0.0);
        double maxMdd = ctxList.stream().mapToDouble(c -> Math.abs(safeDouble(c.getMaxDrawdown()))).max().orElse(1.0);
        double minVol = ctxList.stream().mapToDouble(c -> safeDouble(c.getStandardDeviation()) * 100).min().orElse(0.0);
        double maxVol = ctxList.stream().mapToDouble(c -> safeDouble(c.getStandardDeviation()) * 100).max().orElse(1.0);
        double minReturn = ctxList.stream().mapToDouble(c -> safeDouble(c.getPriceChange())).min().orElse(0.0);
        double maxReturn = ctxList.stream().mapToDouble(c -> safeDouble(c.getPriceChange())).max().orElse(1.0);

        // Genel performans skoruna göre ilk 3 fon
        List<ReportContext> top3ByCompositeScore = ctxList.stream()
                .sorted(Comparator.comparingDouble(
                        c -> -combinedScore(c, minSharpe, maxSharpe, minMdd, maxMdd, minVol, maxVol, minReturn, maxReturn)))
                .limit(3)
                .toList();

        summary.append("En iyi genel performans skoruna sahip ilk 3 fon:\n");
        for (ReportContext c : top3ByCompositeScore) {
            double sharpe = safeDouble(c.getSharpeRatio());
            double mdd = safeDouble(c.getMaxDrawdown()) * 100;
            double volatility = safeDouble(c.getStandardDeviation()) * 100;
            double returnPct = safeDouble(c.getPriceChange()) * 100;

            double score = combinedScore(c, minSharpe, maxSharpe, minMdd, maxMdd, minVol, maxVol, minReturn, maxReturn);

            summary.append(String.format(" - %s (Skor: %.2f, Sharpe: %.2f, MDD: %.2f%%, Return: %.2f%%, Volatilite: %.2f%%)%n",
                    c.getFundMetaData().getCode(),
                    score,
                    sharpe,
                    mdd,
                    returnPct,
                    volatility));
        }

        return summary.toString();
    }

    private double safeDouble(BigDecimal value) {
        return value != null ? value.doubleValue() : 0.0;
    }

    private double combinedScore(ReportContext ctx,
                                 double minSharpe, double maxSharpe,
                                 double minMdd, double maxMdd,
                                 double minVol, double maxVol,
                                 double minReturn, double maxReturn) {

        double sharpe = safeDouble(ctx.getSharpeRatio());
        double mdd = Math.abs(safeDouble(ctx.getMaxDrawdown()));
        double vol = safeDouble(ctx.getStandardDeviation()) * 100;
        double returnPct = safeDouble(ctx.getPriceChange());

        double sharpeNorm = (sharpe - minSharpe) / (maxSharpe - minSharpe + 1e-6);
        double mddNorm = 1.0 - (mdd - minMdd) / (maxMdd - minMdd + 1e-6);
        double volNorm = 1.0 - (vol - minVol) / (maxVol - minVol + 1e-6); // düşük vol daha iyi
        double returnNorm = (returnPct - minReturn) / (maxReturn - minReturn + 1e-6);

        // Ağırlıklar: Sharpe %35, MDD %25, Getiri %25, Volatilite %15
        return 0.35 * sharpeNorm + 0.25 * mddNorm + 0.25 * returnNorm + 0.15 * volNorm;
    }
}
