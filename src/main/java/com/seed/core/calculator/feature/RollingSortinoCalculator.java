package com.seed.core.calculator.feature;

import com.seed.core.AnalysisContext;
import com.seed.core.calculator.Calculator;
import com.seed.core.model.HistoricalData;
import com.seed.core.model.ResultKey;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

import static com.seed.core.calculator.base.DailyPriceChange.DAILY_PRICE_CHANGE;
import static com.seed.core.calculator.feature.RollingReturnCalculator.RET_3M;

public class RollingSortinoCalculator<H extends HistoricalData>
        implements Calculator<H> {

    public static final ResultKey<BigDecimal> SORTINO_3M =
            ResultKey.of("Sortino.3M", BigDecimal.class);

    private static final int WINDOW = 63;
    private static final double MIN_VOL = 0.0005;

    @Override
    public List<ResultKey<?>> requires() {
        return List.of(
                DAILY_PRICE_CHANGE,
                RET_3M
        );
    }

    @Override
    public List<ResultKey<?>> produces() {
        return List.of(SORTINO_3M);
    }

    @Override
    public Map<ResultKey<?>, Object> calculate(AnalysisContext<?, H> ctx) {

        List<BigDecimal> dailyReturns =
                ctx.get(DAILY_PRICE_CHANGE)
                        .orElseThrow(() -> new IllegalStateException("Missing DAILY_PRICE_CHANGE"));

        BigDecimal ret3m =
                ctx.get(RET_3M)
                        .orElseThrow(() -> new IllegalStateException("Missing RET_3M"));

        double downsideVol = rollingDownsideVolatility(dailyReturns, WINDOW);

        if (downsideVol <= 0.0) {
            return Map.of(SORTINO_3M, BigDecimal.ZERO);
        }

        // Annualize downside volatility
        downsideVol *= Math.sqrt(252);

        // Geometric mean daily return
        double meanDailyReturn =
                Math.pow(1.0 + ret3m.doubleValue(), 1.0 / WINDOW) - 1.0;

        // Annualized return
        double annualizedReturn = meanDailyReturn * 252;

        double sortino = annualizedReturn / downsideVol;

        return Map.of(SORTINO_3M, BigDecimal.valueOf(sortino));
    }

    private double rollingDownsideVolatility(List<BigDecimal> daily, int window) {

        int n = daily.size();
        if (n < window) {
            return 0.0;
        }

        int start = n - window;

        double sumSq = 0.0;
        int count = 0;

        for (int i = start; i < n; i++) {
            double r = daily.get(i).doubleValue();

            if (r < 0.0) {
                sumSq += r * r;
                count++;
            }
        }

        if (count < 5) {
            return MIN_VOL;
        }

        return Math.sqrt(sumSq / (count - 1));
    }
}
