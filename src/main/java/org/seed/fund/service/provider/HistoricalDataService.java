package org.seed.fund.service.provider;

import org.seed.fund.model.ExternalFundHistoricalData;
import org.seed.fund.model.ServiceResponse;

import java.time.LocalDate;
import java.util.List;

public interface HistoricalDataService {
    ServiceResponse<List<ExternalFundHistoricalData>> retrieveList(LocalDate valueDate);
}
