package com.seed.core.model;

import java.math.BigDecimal;
import java.time.LocalDate;

public interface Candle {
    LocalDate date();
    BigDecimal price();
}
