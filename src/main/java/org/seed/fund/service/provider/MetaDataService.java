package org.seed.fund.service.provider;

import org.seed.fund.model.ExternalFundMetaData;
import org.seed.fund.model.ServiceResponse;

import java.util.List;

public interface MetaDataService {
    ServiceResponse<List<ExternalFundMetaData>> retrieveList();
}
