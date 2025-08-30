package org.seed.fund.report.model;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.math.BigDecimal;
import java.time.LocalDate;

@AllArgsConstructor
@Getter
public class HistoricalPrice {
    private final LocalDate valueDate;
    private final BigDecimal price;
    private final BigDecimal change;
}
