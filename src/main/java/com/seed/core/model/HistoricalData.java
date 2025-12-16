package com.seed.core.model;

import java.math.BigDecimal;
import java.time.LocalDate;

public interface HistoricalData {
    LocalDate date();
    BigDecimal price();
}
