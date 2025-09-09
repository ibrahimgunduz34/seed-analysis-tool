package org.seed.stock.model;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Getter
@AllArgsConstructor
public class StockHistoricalData {
    private final Long id;
    private final LocalDate valueDate;
    private final BigDecimal price;
    private final LocalDateTime createdAt;
}
