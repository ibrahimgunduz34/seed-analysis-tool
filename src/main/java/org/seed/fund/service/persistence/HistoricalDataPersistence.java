package org.seed.fund.service.persistence;

import org.seed.fund.model.ExternalFundHistoricalData;

import java.time.LocalDate;
import java.util.List;

public interface HistoricalDataPersistence {
    void persist(LocalDate valueDate, List<ExternalFundHistoricalData> externalFundHistoricalData);
}
