package org.seed.fund.api.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.math.BigDecimal;

@AllArgsConstructor
@Getter
public class StrategyData {
    private final BigDecimal totalCost;
    private final BigDecimal totalValue;
    private final BigDecimal profit;
    private final Double profitPercentage;
}
