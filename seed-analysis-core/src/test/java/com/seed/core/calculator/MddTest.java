package com.seed.core.calculator;

import com.seed.core.AnalysisContext;
import com.seed.core.HistoricalData;
import com.seed.core.ResultKey;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;

class MddTest {
    private Mdd<DummyCandle> calculator;

    @BeforeEach
    void setUp() {
        calculator = new Mdd<>();
    }

    @Test
    @DisplayName("Mdd.calculate() : should calculate mdd")
    void calculate() {
        AnalysisContext<DummyCandle> ctx = new AnalysisContext<>(new HistoricalData<>(List.of(
                new DummyCandle(LocalDate.now(), BigDecimal.valueOf(100)),
                new DummyCandle(LocalDate.now(), BigDecimal.valueOf(150)),
                new DummyCandle(LocalDate.now(), BigDecimal.valueOf(180)),
                new DummyCandle(LocalDate.now(), BigDecimal.valueOf(130))
        )));

        Map<ResultKey<?>, Object> result = calculator.calculate(ctx);

        assertThat(result)
                .containsKey(Mdd.MDD)
                .satisfies(map -> {
                    assertThat(map.get(Mdd.MDD)).isEqualTo(BigDecimal.valueOf(0.2777777778).negate());
                });
    }

    // TODO: Create negative test cases
}