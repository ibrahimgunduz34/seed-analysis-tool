package com.seed.core.calculator;

import com.seed.core.AnalysisContext;
import com.seed.core.HistoricalData;
import com.seed.core.ResultKey;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDate;
import java.util.List;
import java.util.Map;
import java.util.stream.Stream;

import static org.assertj.core.api.Assertions.assertThat;

class PeriodPriceChangeTest {
    private PeriodPriceChange<DummyCandle> calculator;

    @BeforeEach
    void setUp() {
        calculator = new PeriodPriceChange<>();
    }

    static Stream<Arguments> getPriceData() {
        return Stream.of(
                Arguments.of(
                        "with falling price",
                        List.of(
                                new DummyCandle(LocalDate.now(), BigDecimal.valueOf(100)),
                                new DummyCandle(LocalDate.now(), BigDecimal.valueOf(150)),
                                new DummyCandle(LocalDate.now(), BigDecimal.valueOf(90))
                        ),
                        BigDecimal.valueOf(0.1).negate().setScale(10, RoundingMode.HALF_UP)
                ),
                Arguments.of(
                        "with rising price",
                        List.of(
                                new DummyCandle(LocalDate.now(), BigDecimal.valueOf(100)),
                                new DummyCandle(LocalDate.now(), BigDecimal.valueOf(150)),
                                new DummyCandle(LocalDate.now(), BigDecimal.valueOf(210))
                        ),
                        BigDecimal.valueOf(1.1).setScale(10, RoundingMode.HALF_UP)
                )
        );
    }

    @ParameterizedTest(
            name = "PeriodPriceChangeTest.calculate() : should calculate price change in the period - {0}"
    )
    @MethodSource("getPriceData")
    void calculateWithFalling(String caseName, List<DummyCandle> priceData, BigDecimal expectedValue) {
        AnalysisContext<DummyCandle> ctx = new AnalysisContext<>(new HistoricalData<>(priceData));

        Map<ResultKey<?>, Object> result = calculator.calculate(ctx);

        assertThat(result)
                .containsKey(PeriodPriceChange.PERIOD_PRICE_CHANGE)
                .satisfies(map -> {
                   assertThat(map.get(PeriodPriceChange.PERIOD_PRICE_CHANGE))
                           .isEqualTo(expectedValue);
                });
    }

    // TODO: Create negative test cases
}