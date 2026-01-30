package com.seed.core.calculator.feature;

import com.seed.core.AnalysisContext;
import com.seed.core.calculator.Calculator;
import com.seed.core.model.HistoricalData;
import com.seed.core.model.ResultKey;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

import static com.seed.core.calculator.base.DailyPriceChange.DAILY_PRICE_CHANGE;

public class RollingVolatilityCalculator<H extends HistoricalData>
        implements Calculator<H> {

    public static final ResultKey<BigDecimal> VOL_21D =
            ResultKey.of("Volatility.21D", BigDecimal.class);

    private static final int WINDOW = 21;
    private static final BigDecimal MIN_VOL = BigDecimal.valueOf(0.005);

    @Override
    public List<ResultKey<?>> requires() {
        return List.of(DAILY_PRICE_CHANGE);
    }

    @Override
    public List<ResultKey<?>> produces() {
        return List.of(VOL_21D);
    }

    @Override
    public Map<ResultKey<?>, Object> calculate(AnalysisContext<?, H> ctx) {

        List<BigDecimal> dailyReturns =
                ctx.get(DAILY_PRICE_CHANGE)
                        .orElseThrow(() -> new IllegalStateException("Missing DAILY_PRICE_CHANGE"));

        BigDecimal vol = rollingStdDev(dailyReturns, WINDOW);

        return Map.of(VOL_21D, vol);
    }

    private BigDecimal rollingStdDev(List<BigDecimal> data, int window) {

        int n = data.size();
        if (n < window) {
            return BigDecimal.ZERO;
        }

        int start = n - window;

        double mean = 0.0;
        double m2 = 0.0;
        int count = 0;

        for (int i = start; i < n; i++) {
            double x = data.get(i).doubleValue();
            count++;

            double delta = x - mean;
            mean += delta / count;
            double delta2 = x - mean;
            m2 += delta * delta2;
        }

        double variance = (count > 1) ? (m2 / (count - 1)) : 0.0;

        BigDecimal vol = BigDecimal.valueOf(Math.sqrt(variance) * Math.sqrt(252));
        return vol.max(MIN_VOL);
    }
}
