package com.seed.fund.model;

import com.seed.core.model.Candle;

import java.math.BigDecimal;
import java.time.LocalDate;

public record FundCandle(
        LocalDate date,
        BigDecimal price
) implements Candle {}
