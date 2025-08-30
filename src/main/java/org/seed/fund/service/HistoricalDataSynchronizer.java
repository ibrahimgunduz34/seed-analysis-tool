package org.seed.fund.service;

import java.time.LocalDate;

public interface HistoricalDataSynchronizer {
    void synchronize(LocalDate valueDate);
    void synchronize(LocalDate beginDate, LocalDate endDate);
}
