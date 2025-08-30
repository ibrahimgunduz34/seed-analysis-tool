package org.seed.fund.api.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.math.BigDecimal;
import java.time.LocalDate;

@AllArgsConstructor
@Getter
public class InvestmentData {
    private final BigDecimal initialAmount;
    private final Integer frequency;
    private final LocalDate startDate;
    private final LocalDate endDate;
}
