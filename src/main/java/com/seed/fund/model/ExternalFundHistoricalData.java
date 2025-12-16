package com.seed.fund.model;

import java.math.BigDecimal;
import java.time.LocalDate;

public record ExternalFundHistoricalData(
        String code,
        BigDecimal numberOfShares,
        Integer numberOfInvestors,
        BigDecimal totalValue,
        BigDecimal price,
        LocalDate valueDate
) {
}
