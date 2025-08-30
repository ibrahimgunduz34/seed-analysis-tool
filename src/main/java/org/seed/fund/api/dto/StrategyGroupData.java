package org.seed.fund.api.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public class StrategyGroupData {
    private final StrategyData fixedQuantity;
    private final StrategyData fixedCost;
    private final StrategyData variableCost;
}
