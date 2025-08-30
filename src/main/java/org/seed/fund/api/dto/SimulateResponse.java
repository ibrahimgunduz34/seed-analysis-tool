package org.seed.fund.api.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public class SimulateResponse {
    private final FundMetaData metaData;
    private final InvestmentData investmentData;
    private final StrategyGroupData strategies;
}
