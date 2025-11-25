package com.seed.fund.model;

import com.seed.core.Asset;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.util.List;

@Getter
@RequiredArgsConstructor
public class Fund implements Asset<FundMetaData, FundHistoricalData> {
    private final Long id;
    private final FundMetaData metaData;
    private final List<FundHistoricalData> historicalData;
}
