package org.seed.fund.service;

import org.seed.fund.model.ExternalHistoricalData;
import org.seed.fund.model.ServiceResponse;

import java.time.LocalDate;
import java.util.List;

public interface HistoricalDataService {
    ServiceResponse<List<ExternalHistoricalData>> retrieveList(LocalDate valueDate);
}
