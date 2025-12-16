package com.seed.fund.model;

import com.seed.core.model.Asset;

import java.util.List;

public record Fund(
        FundMetaData metaData,
        List<FundHistoricalData> historicalData
) implements Asset<FundMetaData, FundHistoricalData> {}
