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

        if (isBuy(finalScore, trend, risk, mdd)) {
            decision = Decision.BUY;
        } else if (isSell(finalScore, trend, mdd)) {
            decision = Decision.SELL;
        } else {
            decision = Decision.HOLD;
        }

        int confidence = computeConfidence(finalScore, trend, risk, mdd, decision);

        return Map.of(
                TRADE_SIGNAL, decision,
                CONFIDENCE, confidence
        );
    }

    /* ---------------- Decision Rules ---------------- */

    private boolean isBuy(int finalScore, int trend, int risk, double mdd) {
        return finalScore >= 75 &&
                trend >= 1 &&
                risk >= 55 &&
                mdd <= 0.18;
    }

    private boolean isSell(int finalScore, int trend, double mdd) {
        return finalScore <= 45 ||
                trend <= -1 ||
                mdd >= 0.22;
    }

    /* ---------------- Confidence Model ---------------- */

    private int computeConfidence(
            int finalScore,
            int trend,
            int risk,
            double mdd,
            Decision decision
    ) {

        double trendStrength   = clamp((trend + 2) / 4.0, 0, 1);
        double momentumStrength = clamp(finalScore / 100.0, 0, 1);
        double riskStability   = clamp(risk / 100.0, 0, 1);
        double drawdownSafety = clamp(1.0 - (mdd / 0.30), 0, 1);

        double baseConfidence =
                0.35 * trendStrength +
                        0.30 * momentumStrength +
                        0.20 * riskStability +
                        0.15 * drawdownSafety;

        // Decision-specific tuning
        switch (decision) {
            case BUY -> baseConfidence *= 1.05;
            case SELL -> baseConfidence *= 1.10;
            case HOLD -> baseConfidence *= 0.95;
        }

        int confidence = (int) Math.round(baseConfidence * 100);

        // Minimum confidence floors
        if (decision == Decision.BUY)  confidence = Math.max(confidence, 55);
        if (decision == Decision.SELL) confidence = Math.max(confidence, 60);
        if (decision == Decision.HOLD) confidence = Math.max(confidence, 45);

        return clamp(confidence, 0, 100);
    }

    /* ---------------- Helpers ---------------- */

    private double clamp(double v, double min, double max) {
        return Math.max(min, Math.min(max, v));
    }

    private int clamp(int v, int min, int max) {
        return Math.max(min, Math.min(max, v));
    }
}
