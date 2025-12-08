package com.seed.core.calculator;

import com.seed.core.model.Candle;

import java.math.BigDecimal;
import java.time.LocalDate;

public record DummyCandle(
        LocalDate date,
        BigDecimal price
) implements Candle {}
