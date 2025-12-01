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

class PositiveNegativeDaysTest {
    private PositiveNegativeDays<DummyCandle> calculator;

    @BeforeEach
    void setUp() {
        calculator = new PositiveNegativeDays<>();
    }

    @Test
    @DisplayName("PositiveNegativeDays.calculate() : should calculate number and weight of positive/negative days")
    void calculate() {
        AnalysisContext<DummyCandle> ctx = new AnalysisContext<>(new HistoricalData<>(List.of()));

        ctx.putIfAbsent(DailyPriceChange.DAILY_PRICE_CHANGE, List.of(
                BigDecimal.valueOf(0.5).setScale(10, RoundingMode.HALF_UP),
                BigDecimal.valueOf(0.2).setScale(10, RoundingMode.HALF_UP),
                BigDecimal.valueOf(0.333333333).negate().setScale(10, RoundingMode.HALF_UP)
        ));

        Map<ResultKey<?>, Object> result = calculator.calculate(ctx);

        assertThat(result)
                .containsKey(PositiveNegativeDays.NUMBER_OF_POSITIVE_DAYS)
                .containsKey(PositiveNegativeDays.NUMBER_OF_NEGATIVE_DAYS)
                .containsKey(PositiveNegativeDays.WEIGHT_OF_POSITIVE_DAYS)
                .containsKey(PositiveNegativeDays.WEIGHT_OF_NEGATIVE_DAYS)
                .satisfies(map -> {
                    assertThat(map.get(PositiveNegativeDays.NUMBER_OF_POSITIVE_DAYS))
                            .isEqualTo(2);

                    assertThat(map.get(PositiveNegativeDays.NUMBER_OF_NEGATIVE_DAYS))
                            .isEqualTo(1);

                    assertThat(map.get(PositiveNegativeDays.WEIGHT_OF_POSITIVE_DAYS))
                            .isEqualTo(0.6666666666666666);

                    assertThat(map.get(PositiveNegativeDays.WEIGHT_OF_NEGATIVE_DAYS))
                            .isEqualTo(0.3333333333333333);
                });

    }

    // TODO: Create negative test cases
}