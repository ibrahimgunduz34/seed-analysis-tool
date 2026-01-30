package com.seed.core.calculator.score;

import com.seed.core.AnalysisContext;
import com.seed.core.calculator.Calculator;
import com.seed.core.model.HistoricalData;
import com.seed.core.model.ResultKey;

import java.util.List;
import java.util.Map;

import static com.seed.core.calculator.score.MomentumScoreCalculator.MOMENTUM_SCORE;
import static com.seed.core.calculator.score.RiskScoreCalculator.RISK_SCORE;

public class FinalScoreCalculator<H extends HistoricalData>
        implements Calculator<H> {

    public static final ResultKey<Integer> FINAL_SCORE =
            ResultKey.of("Final.Score", Integer.class);

    @Override
    public List<ResultKey<?>> requires() {
        return List.of(
                MOMENTUM_SCORE,
                RISK_SCORE
        );
    }

    @Override
    public List<ResultKey<?>> produces() {
        return List.of(FINAL_SCORE);
    }

    @Override
    public Map<ResultKey<?>, Object> calculate(AnalysisContext<?, H> ctx) {

        int momentum =
                ctx.get(MOMENTUM_SCORE)
                        .orElseThrow(() -> new IllegalStateException("Missing MOMENTUM_SCORE"));

        int risk =
                ctx.get(RISK_SCORE)
                        .orElseThrow(() -> new IllegalStateException("Missing RISK_SCORE"));

        int finalScore = clamp(
                (int) Math.round(0.60 * momentum + 0.40 * risk),
                0,
                100
        );

        return Map.of(FINAL_SCORE, finalScore);
    }

    private int clamp(int v, int min, int max) {
        return Math.max(min, Math.min(max, v));
    }
}
