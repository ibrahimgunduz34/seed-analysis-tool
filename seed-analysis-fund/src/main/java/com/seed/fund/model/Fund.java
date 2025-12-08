package com.seed.fund.model;

import com.seed.core.model.Asset;
import com.seed.core.model.HistoricalData;

public record Fund(
        FundMetaData getMetaData,
        HistoricalData<FundCandle> historicalData
) implements Asset<FundMetaData, FundCandle> {}
