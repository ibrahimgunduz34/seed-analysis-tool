package com.seed.core;

import java.math.BigDecimal;
import java.time.LocalDate;

public interface Candle {
    LocalDate date();
    BigDecimal price();
}
