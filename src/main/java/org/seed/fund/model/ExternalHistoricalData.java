package org.seed.fund.model;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.math.BigDecimal;
import java.time.LocalDate;

@AllArgsConstructor
@Getter
public class ExternalHistoricalData {
    private final MetaData metaData;
    private final BigDecimal numberOfShares;
    private final Integer numberOfInvestors;
    private final BigDecimal totalValue;
    private final BigDecimal price;
    private final LocalDate valueDate;

    public HistoricalData toModel() {
        return HistoricalData.create(
                numberOfShares,
                numberOfInvestors,
                totalValue,
                price,
                valueDate
        );
    }
}
