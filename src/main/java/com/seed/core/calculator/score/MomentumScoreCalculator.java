package com.seed.core.calculator.score;

import com.seed.core.AnalysisContext;
import com.seed.core.calculator.Calculator;
import com.seed.core.model.HistoricalData;
import com.seed.core.model.ResultKey;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

import static com.seed.core.calculator.feature.RollingReturnCalculator.RET_1M;
import static com.seed.core.calculator.feature.RollingReturnCalculator.RET_3M;
import static com.seed.core.calculator.feature.RollingSharpeCalculator.SHARPE_3M;
import static com.seed.core.calculator.feature.RollingSortinoCalculator.SORTINO_3M;
import static com.seed.core.calculator.trend.TrendSignalCalculator.TREND_SIGNAL;

public class MomentumScoreCalculator<H extends HistoricalData>
        implements Calculator<H> {

    public static final ResultKey<Integer> MOMENTUM_SCORE =
            ResultKey.of("Momentum.Score", Integer.class);

    @Override
    public List<ResultKey<?>> requires() {
        return List.of(
                RET_1M,
                RET_3M,
                SHARPE_3M,
                SORTINO_3M,
                TREND_SIGNAL
        );
    }

    @Override
    public List<ResultKey<?>> produces() {
        return List.of(MOMENTUM_SCORE);
    }

    @Override
    public Map<ResultKey<?>, Object> calculate(AnalysisContext<?, H> ctx) {

        double ret1m =
                ctx.get(RET_1M).orElse(BigDecimal.ZERO).doubleValue();

        double ret3m =
                ctx.get(RET_3M).orElse(BigDecimal.ZERO).doubleValue();

        double sharpe =
                ctx.get(SHARPE_3M).orElse(BigDecimal.ZERO).doubleValue();

        double sortino =
                ctx.get(SORTINO_3M).orElse(BigDecimal.ZERO).doubleValue();

        int trend =
                ctx.get(TREND_SIGNAL).orElse(0);

        double score =
                0.25 * normReturn(ret1m) +
                        0.20 * normReturn(ret3m) +
                        0.20 * normSharpe(sharpe) +
                        0.15 * normSortino(sortino) +
                        0.20 * normTrend(trend);

        int finalScore = clamp((int) Math.round(score * 100.0), 0, 100);

        return Map.of(MOMENTUM_SCORE, finalScore);
    }

    /* ---------------- Normalizers ---------------- */

    /**
     * Return normalization:
     *  - -10% → 0
     *  - +30% → 1
     */
    private double normReturn(double r) {
        return clamp((r + 0.10) / 0.40, 0.0, 1.0);
    }

    /**
     * Sharpe normalization:
     *  sigmoid style clamp
     *  0 → 0.2
     *  1 → 0.6
     *  2 → 0.85
     *  3 → 0.95
     */
    private double normSharpe(double s) {
        return clamp(s / 3.0, 0.0, 1.0);
    }

    /**
     * Sortino normalization:
     *  identical scaling to Sharpe
     */
    private double normSortino(double s) {
        return clamp(s / 3.0, 0.0, 1.0);
    }

    /**
     * Trend normalization:
     *  -2 → 0
     *  -1 → 0.25
     *   0 → 0.50
     *  +1 → 0.75
     *  +2 → 1.00
     */
    private double normTrend(int t) {
        return clamp((t + 2.0) / 4.0, 0.0, 1.0);
    }

    private double clamp(double v, double min, double max) {
        return Math.max(min, Math.min(max, v));
    }

    private int clamp(int v, int min, int max) {
        return Math.max(min, Math.min(max, v));
    }
}
