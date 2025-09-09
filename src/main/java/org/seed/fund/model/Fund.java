package org.seed.fund.model;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.Optional;

@AllArgsConstructor
@Getter
public class Fund {
    private final Long id;
    private final FundMetaData fundMetaData;
    private final FundHistoricalData additionalData;

    public Optional<FundHistoricalData> getAdditionalData() {
        return Optional.ofNullable(additionalData);
    }

    public static Fund create(FundMetaData fundMetaData, FundHistoricalData fundHistoricalData) {
        return new Fund(
                fundMetaData.getId(),
                fundMetaData,
                fundHistoricalData
        );
    }
}
