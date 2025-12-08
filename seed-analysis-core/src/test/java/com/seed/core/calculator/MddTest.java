package com.seed.core.calculator;

import com.seed.core.AnalysisContext;
import com.seed.core.model.Candle;
import com.seed.core.model.HistoricalData;
import com.seed.core.model.MetaData;
import com.seed.core.model.ResultKey;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;

class MddTest {
    private Mdd<Candle> calculator;

    @BeforeEach
    void setUp() {
        calculator = new Mdd<>();
    }

    @Test
    @DisplayName("Mdd.calculate() : should calculate mdd")
    void calculate() {
        MetaData metaData = Mockito.mock(MetaData.class);
        AnalysisContext<MetaData, Candle> ctx = new AnalysisContext<>(metaData, new HistoricalData<>(List.of(
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