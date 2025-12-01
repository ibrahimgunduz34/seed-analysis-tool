package com.seed.core.calculator;

import com.seed.core.AnalysisContext;
import com.seed.core.HistoricalData;
import com.seed.core.ResultKey;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.List;
import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;

class StDevTest {
    private StDev<DummyCandle> calculator;

    @BeforeEach
    void setUp() {
        calculator = new StDev<>();
    }

    @Test
    @DisplayName("StDev.calculate() : should calculate sample standard deviation of daily price changes")
    void calculate() {
        AnalysisContext<DummyCandle> ctx = new AnalysisContext<>(new HistoricalData<>(List.of()));

        ctx.putIfAbsent(DailyPriceChange.DAILY_PRICE_CHANGE, List.of(
                BigDecimal.valueOf(0.5).setScale(10, RoundingMode.HALF_UP),
                BigDecimal.valueOf(0.2).setScale(10, RoundingMode.HALF_UP),
                BigDecimal.valueOf(0.333333333).negate().setScale(10, RoundingMode.HALF_UP)
        ));

        ctx.putIfAbsent(Mean.MEAN, BigDecimal.valueOf(0.122222222));

        Map<ResultKey<?>, Object> result = calculator.calculate(ctx);

        assertThat(result)
                .containsKey(StDev.ST_DEV)
                .satisfies(map -> {
                    assertThat(map.get(StDev.ST_DEV))
                            .isEqualTo(BigDecimal.valueOf(0.4220759979));;
                });
    }

    // TODO: Create negative test cases
}