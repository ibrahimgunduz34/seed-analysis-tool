package org.seed.fund.model;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.Optional;

@AllArgsConstructor
@Getter
public class Fund {
    private final Long id;
    private final MetaData metaData;
    private final HistoricalData additionalData;

    public Optional<HistoricalData> getAdditionalData() {
        return Optional.ofNullable(additionalData);
    }

    public static Fund create(MetaData metaData, HistoricalData historicalData) {
        return new Fund(
                metaData.getId(),
                metaData,
                historicalData
        );
    }
}
