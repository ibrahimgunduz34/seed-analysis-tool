package com.seed.core;

import com.seed.core.model.ServiceResponse;

import java.time.LocalDate;
import java.util.List;

public interface DataProvider<M, H> {
    ServiceResponse<List<M>> exportMetaData();
    ServiceResponse<List<H>> exportHistoricalData(LocalDate valueDate);
}
