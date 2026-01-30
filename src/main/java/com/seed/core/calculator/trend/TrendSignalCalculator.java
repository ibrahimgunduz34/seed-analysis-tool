package com.seed.core.calculator.trend;

import com.seed.core.AnalysisContext;
import com.seed.core.calculator.Calculator;
import com.seed.core.model.HistoricalData;
import com.seed.core.model.ResultKey;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

import static com.seed.core.calculator.feature.Ema20Calculator.EMA_20;
import static com.seed.core.calculator.feature.Ema50Calculator.EMA_50;
import static com.seed.core.calculator.feature.Ema100Calculator.EMA_100;

public class TrendSignalCalculator<H extends HistoricalData>
        implements Calculator<H> {

    public static final ResultKey<Integer> TREND_SIGNAL =
            ResultKey.of("Trend.Signal", Integer.class);

    public static final ResultKey<String> TREND_LABEL =
            ResultKey.of("Trend.Label", String.class);

    @Override
    public List<ResultKey<?>> requires() {
        return List.of(EMA_20, EMA_50, EMA_100);
    }

    @Override
    public List<ResultKey<?>> produces() {
        return List.of(TREND_SIGNAL, TREND_LABEL);
    }

    @Override
    public Map<ResultKey<?>, Object> calculate(AnalysisContext<?, H> ctx) {

        BigDecimal ema20 =
                ctx.get(EMA_20)
                        .orElseThrow(() -> new IllegalStateException("Missing EMA_20"));

        BigDecimal ema50 =
                ctx.get(EMA_50)
                        .orElseThrow(() -> new IllegalStateException("Missing EMA_50"));

        BigDecimal ema100 =
                ctx.get(EMA_100)
                        .orElseThrow(() -> new IllegalStateException("Missing EMA_100"));

        int signal;
        String label;

        if (ema20.compareTo(ema50) > 0 && ema50.compareTo(ema100) > 0) {
            signal = 2;
            label = "STRONG_UPTREND";
        } else if (ema20.compareTo(ema50) > 0) {
            signal = 1;
            label = "UPTREND";
        } else if (ema20.compareTo(ema50) < 0 && ema50.compareTo(ema100) < 0) {
            signal = -2;
            label = "STRONG_DOWNTREND";
        } else if (ema20.compareTo(ema50) < 0) {
            signal = -1;
            label = "DOWNTREND";
        } else {
            signal = 0;
            label = "SIDEWAYS";
        }

        return Map.of(
                TREND_SIGNAL, signal,
                TREND_LABEL, label
        );
    }
}
