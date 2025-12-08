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

class MeanTest {
    private Mean<Candle> calculator;

    @BeforeEach
    void setUp() {
        calculator = new Mean<>();
    }

    @Test
    @DisplayName("Mean.calculate() : should calculate mean of daily price change")
    void calculate() {
        MetaData metaData = Mockito.mock(MetaData.class);
        AnalysisContext<MetaData, Candle> ctx = new AnalysisContext<>(metaData, new HistoricalData<>(List.of()));

        ctx.putIfAbsent(DailyPriceChange.DAILY_PRICE_CHANGE, List.of(
                BigDecimal.valueOf(0.5).setScale(10, RoundingMode.HALF_UP),
                BigDecimal.valueOf(0.2).setScale(10, RoundingMode.HALF_UP)
        ));

        Map<ResultKey<?>, Object> result = calculator.calculate(ctx);

        assertThat(result)
                .containsKey(Mean.MEAN)
                .satisfies(map -> {
                    assertThat(map.get(Mean.MEAN))
                            .isEqualTo(BigDecimal.valueOf(0.35).setScale(10, RoundingMode.HALF_UP));;
                });
    }

    // TODO: Create negative test cases
}