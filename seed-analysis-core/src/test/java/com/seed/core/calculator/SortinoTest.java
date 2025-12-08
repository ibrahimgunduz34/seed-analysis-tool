package com.seed.core.calculator;

import com.seed.core.AnalysisContext;
import com.seed.core.model.HistoricalData;
import com.seed.core.model.MetaData;
import com.seed.core.model.ResultKey;
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

class SortinoTest {
    private Sortino<HistoricalData> calculator;
    private static final LocalDate DEFAULT_DATE = LocalDate.of(2023, 1, 1);

    @BeforeEach
    void setUp() {
        calculator = new Sortino<>();
    }

    @Test
    @DisplayName("Sortino.calculate() : should calculate sortino")
    void calculate() {
        MetaData metaData = Mockito.mock(MetaData.class);
        AnalysisContext<MetaData, HistoricalData> ctx = new AnalysisContext<>(metaData, List.of(), DEFAULT_DATE, DEFAULT_DATE);
        // prices: 100, 150, 180, 120, 90
        ctx.putIfAbsent(DailyPriceChange.DAILY_PRICE_CHANGE, List.of(
                BigDecimal.valueOf(0.5).setScale(10, RoundingMode.HALF_UP),
                BigDecimal.valueOf(0.2).setScale(10, RoundingMode.HALF_UP),
                BigDecimal.valueOf(0.333333333).negate().setScale(10, RoundingMode.HALF_UP),
                BigDecimal.valueOf(0.25).negate().setScale(10, RoundingMode.HALF_UP)
        ));

        ctx.putIfAbsent(PositiveNegativeDays.NUMBER_OF_NEGATIVE_DAYS, 2);
        ctx.putIfAbsent(Mean.MEAN, BigDecimal.valueOf(0.029166667));

        Map<ResultKey<?>, Object> result = calculator.calculate(ctx);

        assertThat(result)
                .containsKey(Sortino.SORTINO)
                .satisfies(map -> {
                    assertThat(map.get(Sortino.SORTINO))
                            .isEqualTo(BigDecimal.valueOf(0.0700000008));;
                });
    }

    // TODO: Create negative test cases
}
