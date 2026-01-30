package com.seed.core.calculator.feature;

import com.seed.core.AnalysisContext;
import com.seed.core.calculator.Calculator;
import com.seed.core.model.HistoricalData;
import com.seed.core.model.ResultKey;

import java.math.BigDecimal;
import java.math.MathContext;
import java.util.List;
import java.util.Map;

import static com.seed.core.calculator.base.DailyPriceChange.DAILY_PRICE_CHANGE;

public class RollingReturnCalculator<H extends HistoricalData> implements Calculator<H> {
    public static final ResultKey<BigDecimal> RET_1M = ResultKey.of("RET_1M", BigDecimal.class);
    public static final ResultKey<BigDecimal> RET_3M = ResultKey.of("RET_3M", BigDecimal.class);
    public static final ResultKey<BigDecimal> RET_6M = ResultKey.of("RET_6M", BigDecimal.class);
    public static final ResultKey<BigDecimal> RET_12M = ResultKey.of("RET_12M", BigDecimal.class);

    private static final int WIN_1M = 21;
    private static final int WIN_3M = 63;
    private static final int WIN_6M = 126;
    private static final int WIN_12M = 252;

    private static final MathContext MC = MathContext.DECIMAL64;

    @Override
    public List<ResultKey<?>> requires() {
        return List.of(DAILY_PRICE_CHANGE);
    }

    @Override
    public List<ResultKey<?>> produces() {
        return List.of(
                RET_1M,
                RET_3M,
                RET_6M,
                RET_12M
        );
    }

    @Override
    public Map<ResultKey<?>, Object> calculate(AnalysisContext<?, H> ctx) {
        List<BigDecimal> dailyReturns =
                ctx.get(DAILY_PRICE_CHANGE)
                        .orElseThrow(() -> new IllegalStateException("Missing DAILY_PRICE_CHANGE"));


        return Map.of(
                RET_1M, rollingReturn(dailyReturns, WIN_1M),
                RET_3M, rollingReturn(dailyReturns, WIN_3M),
                RET_6M, rollingReturn(dailyReturns, WIN_6M),
                RET_12M, rollingReturn(dailyReturns, WIN_12M)
        );
    }

    private BigDecimal rollingReturn(List<BigDecimal> daily, int window) {
        int n = daily.size();

        if (n < window) {
            return BigDecimal.ZERO;
        }

        BigDecimal acc = BigDecimal.ONE;

        for (int i = n - window; i < n; i++) {
            acc = acc.multiply(
                    BigDecimal.ONE.add(daily.get(i), MC),
                    MC
            );
        }

        return acc.subtract(BigDecimal.ONE, MC);
    }
}
