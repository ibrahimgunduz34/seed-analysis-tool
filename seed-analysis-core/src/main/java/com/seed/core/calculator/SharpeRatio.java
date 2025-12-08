package com.seed.core.calculator;

import com.seed.core.AnalysisContext;
import com.seed.core.model.HistoricalData;
import com.seed.core.model.ResultKey;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.List;
import java.util.Map;

import static com.seed.core.calculator.Mean.MEAN;
import static com.seed.core.calculator.StDev.ST_DEV;

public class SharpeRatio<H extends HistoricalData> implements Calculator<H> {
    public static final ResultKey<BigDecimal> SHARPE_RATIO = ResultKey.of("Sharpe Ratio", BigDecimal.class);

    @Override
    public List<ResultKey<?>> requires() {
        return List.of(
                ST_DEV,
                MEAN
        );
    }

    @Override
    public List<ResultKey<?>> produces() {
        return List.of(
                SHARPE_RATIO
        );
    }

    @Override
    public Map<ResultKey<?>, Object> calculate(AnalysisContext<?, H> ctx) {
        BigDecimal stDev = ctx.get(ST_DEV).orElse(BigDecimal.ZERO);
        BigDecimal mean = ctx.get(MEAN).orElse(BigDecimal.ZERO);

        BigDecimal sharpe = stDev.compareTo(BigDecimal.ZERO) > 0
                ? mean.divide(stDev, 10, RoundingMode.HALF_UP)
                : BigDecimal.ZERO;

        return Map.of(SHARPE_RATIO, sharpe);
    }
}
