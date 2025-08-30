package org.seed.fund.service.persistence;

import org.seed.fund.model.ExternalHistoricalData;

import java.time.LocalDate;
import java.util.List;

public interface HistoricalDataPersistence {
    void persist(LocalDate valueDate, List<ExternalHistoricalData> externalHistoricalData);
}
