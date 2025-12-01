package com.seed.core.calculator;

import com.seed.core.AnalysisContext;
import com.seed.core.HistoricalData;
import com.seed.core.ResultKey;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;

class SharpeRatioTest {
    private SharpeRatio<DummyCandle> calculator;

    @BeforeEach
    void setUp() {
        calculator = new SharpeRatio<>();
    }

    @Test
    @DisplayName("SharpeRatio.calculate() : should calculate sharpe ratio")
    void calculate() {
        AnalysisContext<DummyCandle> ctx = new AnalysisContext<>(new HistoricalData<>(List.of()));

        ctx.putIfAbsent(Mean.MEAN, BigDecimal.valueOf(0.122222222));
        ctx.putIfAbsent(StDev.ST_DEV, BigDecimal.valueOf(0.4220759979));

        Map<ResultKey<?>, Object> result = calculator.calculate(ctx);

        assertThat(result)
                .containsKey(SharpeRatio.SHARPE_RATIO)
                .satisfies(map -> {
                    assertThat(map.get(SharpeRatio.SHARPE_RATIO))
                            .isEqualTo(BigDecimal.valueOf(0.2895739692));;
                });
    }

    // TODO: Create negative test cases
}