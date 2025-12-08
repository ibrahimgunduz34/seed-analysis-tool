package com.seed.core.calculator;

import com.seed.core.AnalysisContext;
import com.seed.core.model.Candle;
import com.seed.core.model.HistoricalData;
import com.seed.core.model.MetaData;
import com.seed.core.model.ResultKey;
import org.assertj.core.api.InstanceOfAssertFactories;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDate;
import java.util.List;
import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;

class DailyPriceChangeTest {
    private DailyPriceChange<Candle> calculator;

    @BeforeEach
    void setUp() {
        calculator = new DailyPriceChange<>();
    }

    @Test
    @DisplayName("DailyPriceChange.calculate() : should calculate daily price change")
    void calculate() {
        MetaData metaData = Mockito.mock(MetaData.class);
        AnalysisContext<MetaData, Candle> ctx = new AnalysisContext<>(metaData, new HistoricalData<>(List.of(
                new DummyCandle(LocalDate.now(), BigDecimal.valueOf(100)),
                new DummyCandle(LocalDate.now(), BigDecimal.valueOf(150)),
                new DummyCandle(LocalDate.now(), BigDecimal.valueOf(180))
        )));

        Map<ResultKey<?>, Object> result = calculator.calculate(ctx);

        assertThat(result)
                .containsKey(DailyPriceChange.DAILY_PRICE_CHANGE)
                .satisfies(map -> {
                    assertThat(map.get(DailyPriceChange.DAILY_PRICE_CHANGE))
                            .asInstanceOf(InstanceOfAssertFactories.list(BigDecimal.class))
                            .containsExactly(
                                    BigDecimal.valueOf(0.5).setScale(10, RoundingMode.HALF_UP),
                                    BigDecimal.valueOf(0.2).setScale(10, RoundingMode.HALF_UP)
                            );
                });
    }

    // TODO: Create negative test cases
}