package com.seed.core;

import java.math.BigDecimal;
import java.time.LocalDate;

public interface HistoricalData {
    LocalDate getDate();
    BigDecimal getPrice();
}
