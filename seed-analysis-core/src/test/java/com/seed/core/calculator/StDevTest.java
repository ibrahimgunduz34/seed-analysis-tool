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
import java.math.RoundingMode;
import java.util.List;
import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;

class StDevTest {
    private StDev<Candle> calculator;

    @BeforeEach
    void setUp() {
        calculator = new StDev<>();
    }

    @Test
    @DisplayName("StDev.calculate() : should calculate sample standard deviation of daily price changes")
    void calculate() {
        MetaData metaData = Mockito.mock(MetaData.class);
        AnalysisContext<MetaData, Candle> ctx = new AnalysisContext<>(metaData, new HistoricalData<>(List.of()));

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