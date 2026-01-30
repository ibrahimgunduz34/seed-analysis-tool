package com.seed.core.calculator.decision;

import com.seed.core.AnalysisContext;
import com.seed.core.calculator.Calculator;
import com.seed.core.model.HistoricalData;
import com.seed.core.model.ResultKey;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

import static com.seed.core.calculator.feature.RollingMddCalculator.MDD_6M;
import static com.seed.core.calculator.score.FinalScoreCalculator.FINAL_SCORE;
import static com.seed.core.calculator.score.RiskScoreCalculator.RISK_SCORE;
import static com.seed.core.calculator.trend.TrendSignalCalculator.TREND_SIGNAL;

public class DecisionCalculator<H extends HistoricalData>
        implements Calculator<H> {

    public enum Decision {
        BUY, HOLD, SELL
    }

    public static final ResultKey<Decision> TRADE_SIGNAL =
            ResultKey.of("Decision.Signal", Decision.class);

    public static final ResultKey<Integer> CONFIDENCE =
            ResultKey.of("Decision.Confidence", Integer.class);

    @Override
    public List<ResultKey<?>> requires() {
        return List.of(
                FINAL_SCORE,
                TREND_SIGNAL,
                RISK_SCORE,
                MDD_6M
        );
    }

    @Override
    public List<ResultKey<?>> produces() {
        return List.of(
                TRADE_SIGNAL,
                CONFIDENCE
        );
    }

    @Override
    public Map<ResultKey<?>, Object> calculate(AnalysisContext<?, H> ctx) {

        int finalScore =
                ctx.get(FINAL_SCORE)
                        .orElseThrow(() -> new IllegalStateException("Missing FINAL_SCORE"));

        int trend =
                ctx.get(TREND_SIGNAL)
                        .orElseThrow(() -> new IllegalStateException("Missing TREND_SIGNAL"));

        int risk =
                ctx.get(RISK_SCORE)
                        .orElseThrow(() -> new IllegalStateException("Missing RISK_SCORE"));

        double mdd =
                ctx.get(MDD_6M)
                        .orElse(BigDecimal.ZERO)
                        .doubleValue();

        Decision decision;
        int confidence;

        if (isBuy(finalScore, trend, risk, mdd)) {
            decision = Decision.BUY;
            confidence = buyConfidence(finalScore, trend, risk, mdd);
        } else if (isSell(finalScore, trend, mdd)) {
            decision = Decision.SELL;
            confidence = sellConfidence(finalScore, trend, mdd);
        } else {
            decision = Decision.HOLD;
            confidence = holdConfidence(finalScore, trend, risk, mdd);
        }

        return Map.of(
                TRADE_SIGNAL, decision,
                CONFIDENCE, confidence
        );
    }

    /* ---------------- Decision Rules ---------------- */

    private boolean isBuy(int finalScore, int trend, int risk, double mdd) {
        return finalScore >= 75 &&
                trend >= 1 &&
                risk >= 60 &&
                mdd <= 0.18;
    }

    private boolean isSell(int finalScore, int trend, double mdd) {
        return finalScore <= 45 ||
                trend <= -1 ||
                mdd >= 0.25;
    }

    /* ---------------- Confidence Functions ---------------- */

    private int buyConfidence(int finalScore, int trend, int risk, double mdd) {

        double scoreFactor = clamp((finalScore - 75) / 25.0, 0, 1);
        double trendFactor = clamp((trend + 2) / 4.0, 0, 1);
        double riskFactor  = clamp((risk - 60) / 40.0, 0, 1);
        double mddFactor   = clamp((0.18 - mdd) / 0.18, 0, 1);

        double conf =
                0.35 * scoreFactor +
                        0.25 * trendFactor +
                        0.25 * riskFactor +
                        0.15 * mddFactor;

        return clamp((int) Math.round(conf * 100), 0, 100);
    }

    private int sellConfidence(int finalScore, int trend, double mdd) {

        double scoreFactor = clamp((45 - finalScore) / 45.0, 0, 1);
        double trendFactor = clamp((-trend + 2) / 4.0, 0, 1);
        double mddFactor   = clamp((mdd - 0.18) / 0.20, 0, 1);

        double conf =
                0.40 * scoreFactor +
                        0.35 * trendFactor +
                        0.25 * mddFactor;

        return clamp((int) Math.round(conf * 100), 0, 100);
    }

    private int holdConfidence(int finalScore, int trend, int risk, double mdd) {

        double neutrality =
                1.0 - Math.abs(finalScore - 60) / 60.0;

        double conf = clamp(neutrality, 0, 1);

        return clamp((int) Math.round(conf * 100), 0, 100);
    }

    /* ---------------- Helpers ---------------- */

    private double clamp(double v, double min, double max) {
        return Math.max(min, Math.min(max, v));
    }

    private int clamp(int v, int min, int max) {
        return Math.max(min, Math.min(max, v));
    }
}
