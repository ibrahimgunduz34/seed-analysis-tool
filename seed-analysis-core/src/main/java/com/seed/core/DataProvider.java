package com.seed.core;

import java.time.LocalDate;

public interface DataProvider<M, H> {
    M exportMetaData();
    H exportHistoricalData(LocalDate valueDate);
}
