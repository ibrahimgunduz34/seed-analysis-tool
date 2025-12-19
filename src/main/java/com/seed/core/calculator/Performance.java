package com.seed.core.calculator;

import com.seed.configuration.ReportConfiguration;
import com.seed.core.AnalysisContext;
import com.seed.core.model.HistoricalData;
import com.seed.core.model.MetaData;
import com.seed.core.model.ResultKey;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import static com.seed.core.calculator.Mdd.MDD;
import static com.seed.core.calculator.PeriodPriceChange.PERIOD_PRICE_CHANGE;
import static com.seed.core.calculator.SharpeRatio.SHARPE_RATIO;

public class Performance<M extends MetaData, H extends HistoricalData> implements BatchCalculator<M, H> {
    public static final ResultKey<Integer> PERFORMANCE_RATING = ResultKey.of("PerformanceRating", Integer.class);

    private final ReportConfiguration reportConfiguration;

    public Performance(ReportConfiguration reportConfiguration) {
        this.reportConfiguration = reportConfiguration;
    }

    @Override
    public List<ResultKey<?>> requires() {
        return List.of(
                SHARPE_RATIO,
                MDD,
                PERIOD_PRICE_CHANGE
        );
    }

    @Override
    public List<ResultKey<?>> produces() {
        return List.of(
                PERFORMANCE_RATING
        );
    }

    @Override
    public List<AnalysisContext<M, H>> calculate(List<AnalysisContext<M, H>> contexts) {
        int maxBarLength = 100;

        // Normalizasyon için min/max değerleri bul
        double minSharpe = contexts.stream().mapToDouble(c -> safeDouble(c.getStatistics().sharpeRatio())).min().orElse(0.0);
        double maxSharpe = contexts.stream().mapToDouble(c -> safeDouble(c.getStatistics().sharpeRatio())).max().orElse(1.0);

        double minMDD = contexts.stream().mapToDouble(c -> Math.abs(safeDouble(c.getStatistics().mdd()))).min().orElse(0.0);
        double maxMDD = contexts.stream().mapToDouble(c -> Math.abs(safeDouble(c.getStatistics().mdd()))).max().orElse(1.0);

        double minReturn = contexts.stream().mapToDouble(c -> safeDouble(c.getStatistics().priceChange())).min().orElse(0.0);
        double maxReturn = contexts.stream().mapToDouble(c -> safeDouble(c.getStatistics().priceChange())).max().orElse(1.0);

        List<AnalysisContext<M, H>> sortedContexts = new ArrayList<>(contexts);

        // Sharpe + MDD + Getiri → ağırlıklı ortalama skor
        sortedContexts.sort((a, b) -> {
            double scoreA = calculateCombinedScore(a, minSharpe, maxSharpe, minMDD, maxMDD, minReturn, maxReturn);
            double scoreB = calculateCombinedScore(b, minSharpe, maxSharpe, minMDD, maxMDD, minReturn, maxReturn);
            return Double.compare(scoreB, scoreA); // descending
        });

        for (AnalysisContext<M, H> ctx : sortedContexts) {
            double combinedScore = calculateCombinedScore(ctx, minSharpe, maxSharpe, minMDD, maxMDD, minReturn, maxReturn);
            int performanceVal = (int) Math.round(combinedScore * maxBarLength);
            performanceVal = Math.max(performanceVal, 1);

            ctx.putIfAbsent(PERFORMANCE_RATING, performanceVal);
        }

        return sortedContexts;
    }

    private double calculateCombinedScore(AnalysisContext<M, H> ctx,
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
