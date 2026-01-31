package com.seed.core.calculator.decision;

import com.seed.configuration.DecisionProperties;
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

    private final DecisionProperties props;

    public DecisionCalculator(DecisionProperties props) {
        this.props = props;
    }

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

        int finalScore = ctx.get(FINAL_SCORE).orElseThrow();
        int trend      = ctx.get(TREND_SIGNAL).orElseThrow();
        int risk       = ctx.get(RISK_SCORE).orElseThrow();
        double mdd     = ctx.get(MDD_6M).orElse(BigDecimal.ZERO).doubleValue();

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
        var b = props.thresholds().buy();
        return finalScore >= b.minFinalScore()
                && trend >= b.minTrend()
                && risk >= b.minRisk()
                && mdd <= b.maxMdd();
    }

    private boolean isSell(int finalScore, int trend, double mdd) {
        var s = props.thresholds().sell();
        return finalScore <= s.maxFinalScore()
                || trend <= s.maxTrend()
                || mdd >= s.minMdd();
    }

    /* ---------------- Confidence ---------------- */

    private int computeConfidence(
            int finalScore,
            int trend,
            int risk,
            double mdd,
            Decision decision
    ) {

        var w = props.confidence().weights();

        double trendStrength    = clamp((trend + 2) / 4.0, 0, 1);
        double momentumStrength = clamp(finalScore / 100.0, 0, 1);
        double riskStability    = clamp(risk / 100.0, 0, 1);
        double drawdownSafety  = clamp(1.0 - (mdd / 0.30), 0, 1);

        double base =
                w.trend()    * trendStrength +
                        w.momentum() * momentumStrength +
                        w.risk()     * riskStability +
                        w.drawdown() * drawdownSafety;

        double multiplier = switch (decision) {
            case BUY  -> props.confidence().multipliers().buy();
            case SELL -> props.confidence().multipliers().sell();
            case HOLD -> props.confidence().multipliers().hold();
        };

        int confidence = (int) Math.round(base * multiplier * 100);

        var floors = props.confidence().floors();
        confidence = switch (decision) {
            case BUY  -> Math.max(confidence, floors.buy());
            case SELL -> Math.max(confidence, floors.sell());
            case HOLD -> Math.max(confidence, floors.hold());
        };

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
