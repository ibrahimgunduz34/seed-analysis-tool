package com.seed.fund.model;

import com.seed.core.model.HistoricalData;

import java.math.BigDecimal;
import java.time.LocalDate;

public record FundHistoricalData(
        LocalDate date,
        BigDecimal price,
        BigDecimal numberOfShares,
        Integer numberOfInvestors,
        BigDecimal totalValue
) implements HistoricalData {}
