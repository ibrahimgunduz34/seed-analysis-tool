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

class GainLossTest {
    private GainLoss<DummyCandle> calculator;

    @BeforeEach
    void setUp() {
        calculator = new GainLoss<>();
    }

    @Test
    @DisplayName("GainLoss.calculate() : should calculate average gain and loss")
    void calculate() {
        AnalysisContext<DummyCandle> ctx = new AnalysisContext<>(new HistoricalData<>(List.of()));

        ctx.putIfAbsent(DailyPriceChange.DAILY_PRICE_CHANGE, List.of(
                BigDecimal.valueOf(0.5).setScale(10, RoundingMode.HALF_UP),
                BigDecimal.valueOf(0.2).setScale(10, RoundingMode.HALF_UP),
                BigDecimal.valueOf(0.333333333).negate().setScale(10, RoundingMode.HALF_UP)
        ));

        ctx.putIfAbsent(PositiveNegativeDays.NUMBER_OF_POSITIVE_DAYS, 2);
        ctx.putIfAbsent(PositiveNegativeDays.NUMBER_OF_NEGATIVE_DAYS, 1);

        Map<ResultKey<?>, Object> result = calculator.calculate(ctx);

        assertThat(result)
                .containsKey(GainLoss.AVERAGE_GAIN)
                .containsKey(GainLoss.AVERAGE_LOSS)
                .satisfies(map -> {
                    assertThat(map.get(GainLoss.AVERAGE_GAIN)).isEqualTo(BigDecimal.valueOf(0.35).setScale(10, RoundingMode.HALF_UP));
                    assertThat(map.get(GainLoss.AVERAGE_LOSS)).isEqualTo(BigDecimal.valueOf(0.3333333330).setScale(10, RoundingMode.HALF_UP));
                });
    }

    // TODO: Create negative test cases
}