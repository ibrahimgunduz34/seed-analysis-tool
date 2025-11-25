package com.seed.fund.model;

import com.seed.core.HistoricalData;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDate;

@Getter
@RequiredArgsConstructor
public class FundHistoricalData implements HistoricalData {
    private final LocalDate date;
    private final BigDecimal price;
}
