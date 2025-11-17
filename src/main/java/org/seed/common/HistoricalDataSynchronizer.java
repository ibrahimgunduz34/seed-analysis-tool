package org.seed.common;

import java.time.LocalDate;

public interface HistoricalDataSynchronizer {
    void synchronize(LocalDate valueDate);
    void synchronize(LocalDate beginDate, LocalDate endDate);
}
