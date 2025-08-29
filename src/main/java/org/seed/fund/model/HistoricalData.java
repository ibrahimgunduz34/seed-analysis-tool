package org.seed.fund.model;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.time.LocalDate;
import java.time.LocalDateTime;

@AllArgsConstructor
@Getter
public class HistoricalData {
    private final Long id;
    private final BigDecimal numberOfShares;
    private final Integer numberOfInvestors;
    private final BigDecimal totalValue;
    private final BigDecimal price;
    private final LocalDate valueDate;
    private final LocalDateTime createdAt;

    public static HistoricalData create(
            BigDecimal numberOfShares,
            Integer numberOfInvestors,
            BigDecimal totalValue,
            BigDecimal price,
            LocalDate valueDate)
    {
        return new HistoricalData(null, numberOfShares, numberOfInvestors, totalValue, price, valueDate, LocalDateTime.now());
    }
}
