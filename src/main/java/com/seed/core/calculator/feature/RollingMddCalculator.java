package com.seed.core.calculator.feature;

import com.seed.core.AnalysisContext;
import com.seed.core.calculator.Calculator;
import com.seed.core.model.HistoricalData;
import com.seed.core.model.ResultKey;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

import static com.seed.core.calculator.base.DailyPriceChange.DAILY_PRICE_CHANGE;

public class RollingMddCalculator<H extends HistoricalData>
        implements Calculator<H> {

    public static final ResultKey<BigDecimal> MDD_6M =
            ResultKey.of("Mdd.6M", BigDecimal.class);

    private static final int WINDOW = 126;

    @Override
    public List<ResultKey<?>> requires() {
        return List.of(DAILY_PRICE_CHANGE);
    }

    @Override
    public List<ResultKey<?>> produces() {
        return List.of(MDD_6M);
    }

    @Override
    public Map<ResultKey<?>, Object> calculate(AnalysisContext<?, H> ctx) {

        List<BigDecimal> dailyReturns =
                ctx.get(DAILY_PRICE_CHANGE)
                        .orElseThrow(() -> new IllegalStateException("Missing DAILY_PRICE_CHANGE"));

        double mdd = rollingMdd(dailyReturns, WINDOW);

        return Map.of(MDD_6M, BigDecimal.valueOf(mdd));
    }

    private double rollingMdd(List<BigDecimal> dailyReturns, int window) {

        int n = dailyReturns.size();
        if (n < window) {
            return 0.0;
        }

        int start = n - window;

        double equity = 1.0;
        double peak = 1.0;
        double maxDrawdown = 0.0;

        for (int i = start; i < n; i++) {
            double r = dailyReturns.get(i).doubleValue();
            equity *= (1.0 + r);

            if (equity > peak) {
                peak = equity;
            }

            double drawdown = (peak - equity) / peak;
            if (drawdown > maxDrawdown) {
                maxDrawdown = drawdown;
            }
        }

        return maxDrawdown;
    }
}
