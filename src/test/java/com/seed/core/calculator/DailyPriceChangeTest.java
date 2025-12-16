package com.seed.core.calculator;

import com.seed.core.AnalysisContext;
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
    private DailyPriceChange<HistoricalData> calculator;
    private static final LocalDate DEFAULT_DATE = LocalDate.of(2023, 1, 1);

    @BeforeEach
    void setUp() {
        calculator = new DailyPriceChange<>();
    }

    @Test
    @DisplayName("DailyPriceChange.calculate() : should calculate daily price change")
    void calculate() {
        MetaData metaData = Mockito.mock(MetaData.class);
        AnalysisContext<MetaData, HistoricalData> ctx = new AnalysisContext<>(metaData, List.of(
                new DummyCandle(LocalDate.now(), BigDecimal.valueOf(100)),
                new DummyCandle(LocalDate.now(), BigDecimal.valueOf(150)),
                new DummyCandle(LocalDate.now(), BigDecimal.valueOf(180))
        ), DEFAULT_DATE, DEFAULT_DATE);

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
