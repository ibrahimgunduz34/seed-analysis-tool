package com.seed.core.calculator.feature;

import com.seed.core.AnalysisContext;
import com.seed.core.calculator.Calculator;
import com.seed.core.model.HistoricalData;
import com.seed.core.model.ResultKey;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

import static com.seed.core.calculator.base.DailyPriceChange.DAILY_PRICE_CHANGE;
import static com.seed.core.calculator.feature.RollingVolatilityCalculator.VOL_21D;
import static com.seed.core.calculator.feature.RollingReturnCalculator.RET_3M;

public class RollingSharpeCalculator<H extends HistoricalData>
        implements Calculator<H> {

    public static final ResultKey<BigDecimal> SHARPE_3M =
            ResultKey.of("Sharpe.3M", BigDecimal.class);

    private static final BigDecimal MIN_VOL = BigDecimal.valueOf(0.005);

    @Override
    public List<ResultKey<?>> requires() {
        return List.of(
                DAILY_PRICE_CHANGE,
                RET_3M,
                VOL_21D
        );
    }

    @Override
    public List<ResultKey<?>> produces() {
        return List.of(SHARPE_3M);
    }

    @Override
    public Map<ResultKey<?>, Object> calculate(AnalysisContext<?, H> ctx) {

        BigDecimal ret3m =
                ctx.get(RET_3M)
                        .orElseThrow(() -> new IllegalStateException("Missing RET_3M"));

        BigDecimal vol21 =
                ctx.get(VOL_21D)
                        .orElseThrow(() -> new IllegalStateException("Missing VOL_21D"));

        double vol = vol21.max(MIN_VOL).doubleValue();

        if (vol == 0.0) {
            return Map.of(SHARPE_3M, BigDecimal.ZERO);
        }

        double meanDailyReturn =
                Math.pow(1.0 + ret3m.doubleValue(), 1.0 / 63.0) - 1.0;

        double annualizedReturn = meanDailyReturn * 252;

        double sharpe = annualizedReturn / vol;

        return Map.of(SHARPE_3M, BigDecimal.valueOf(sharpe));
    }
}
