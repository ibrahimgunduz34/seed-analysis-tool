package com.seed.core.calculator.score;

import com.seed.core.AnalysisContext;
import com.seed.core.calculator.Calculator;
import com.seed.core.model.HistoricalData;
import com.seed.core.model.ResultKey;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

import static com.seed.core.calculator.feature.RollingVolatilityCalculator.VOL_21D;
import static com.seed.core.calculator.feature.RollingMddCalculator.MDD_6M;
import static com.seed.core.calculator.feature.RollingSortinoCalculator.SORTINO_3M;

public class RiskScoreCalculator<H extends HistoricalData>
        implements Calculator<H> {

    public static final ResultKey<Integer> RISK_SCORE =
            ResultKey.of("Risk.Score", Integer.class);

    @Override
    public List<ResultKey<?>> requires() {
        return List.of(
                VOL_21D,
                MDD_6M,
                SORTINO_3M
        );
    }

    @Override
    public List<ResultKey<?>> produces() {
        return List.of(RISK_SCORE);
    }

    @Override
    public Map<ResultKey<?>, Object> calculate(AnalysisContext<?, H> ctx) {

        double vol =
                ctx.get(VOL_21D).orElse(BigDecimal.ZERO).doubleValue();

        double mdd =
                ctx.get(MDD_6M).orElse(BigDecimal.ZERO).doubleValue();

        double sortino =
                ctx.get(SORTINO_3M).orElse(BigDecimal.ZERO).doubleValue();

        double score =
                0.40 * normVolatility(vol) +
                        0.40 * normMdd(mdd) +
                        0.20 * normSortino(sortino);

        int finalScore = clamp((int) Math.round(score * 100.0), 0, 100);

        return Map.of(RISK_SCORE, finalScore);
    }

    /* ---------------- Normalizers ---------------- */

    /**
     * Volatility normalization:
     *  0%  → 1.0  (çok güvenli)
     *  4%  → 0.0  (çok riskli)
     */
    private double normVolatility(double v) {
        return clamp(1.0 - (v / 0.04), 0.0, 1.0);
    }

    /**
     * MDD normalization:
     *  0%  → 1.0  (mükemmel)
     *  30% → 0.0  (çok riskli)
     */
    private double normMdd(double m) {
        return clamp(1.0 - (m / 0.30), 0.0, 1.0);
    }

    /**
     * Sortino normalization:
     *  0 → 0
     *  2 → 1
     */
    private double normSortino(double s) {
        return clamp(s / 2.0, 0.0, 1.0);
    }

    private double clamp(double v, double min, double max) {
        return Math.max(min, Math.min(max, v));
    }

    private int clamp(int v, int min, int max) {
        return Math.max(min, Math.min(max, v));
    }
}
