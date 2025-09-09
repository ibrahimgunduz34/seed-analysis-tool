package org.seed.stock.model;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.math.BigDecimal;
import java.time.LocalDate;

@Getter
@AllArgsConstructor
public class ExternalStockHistoricalData {
    private final LocalDate valueDate;
    private final BigDecimal price;
}
