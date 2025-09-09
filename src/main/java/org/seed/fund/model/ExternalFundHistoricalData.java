package org.seed.fund.model;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.math.BigDecimal;
import java.time.LocalDate;

@AllArgsConstructor
@Getter
public class ExternalFundHistoricalData {
    private final FundMetaData fundMetaData;
    private final BigDecimal numberOfShares;
    private final Integer numberOfInvestors;
    private final BigDecimal totalValue;
    private final BigDecimal price;
    private final LocalDate valueDate;

    public FundHistoricalData toModel() {
        return FundHistoricalData.create(
                numberOfShares,
                numberOfInvestors,
                totalValue,
                price,
                valueDate
        );
    }
}
