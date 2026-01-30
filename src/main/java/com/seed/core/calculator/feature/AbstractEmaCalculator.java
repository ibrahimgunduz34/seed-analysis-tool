package com.seed.core.calculator.feature;

import com.seed.core.AnalysisContext;
import com.seed.core.calculator.Calculator;
import com.seed.core.model.HistoricalData;
import com.seed.core.model.ResultKey;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

public abstract class AbstractEmaCalculator<H extends HistoricalData>
        implements Calculator<H> {

    private final int period;
    private final ResultKey<BigDecimal> resultKey;

    protected AbstractEmaCalculator(int period, ResultKey<BigDecimal> resultKey) {
        this.period = period;
        this.resultKey = resultKey;
    }

    @Override
    public List<ResultKey<?>> requires() {
        return List.of();
    }

    @Override
    public List<ResultKey<?>> produces() {
        return List.of(resultKey);
    }

    @Override
    public Map<ResultKey<?>, Object> calculate(AnalysisContext<?, H> ctx) {

        List<H> series = ctx.getHistoricalData();

        double ema = computeEma(series, period);

        return Map.of(resultKey, BigDecimal.valueOf(ema));
    }

    private double computeEma(List<H> series, int period) {

        int n = series.size();
        if (n < period) {
            return 0.0;
        }

        double alpha = 2.0 / (period + 1.0);

        // İlk EMA = SMA
        double ema = 0.0;
        for (int i = 0; i < period; i++) {
            ema += series.get(i).price().doubleValue();
        }
        ema /= period;

        // EMA recursion
        for (int i = period; i < n; i++) {
            double price = series.get(i).price().doubleValue();
            ema = alpha * price + (1 - alpha) * ema;
        }

        return ema;
    }
}
