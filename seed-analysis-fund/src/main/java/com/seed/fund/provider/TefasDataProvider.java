package com.seed.fund.provider;

import com.seed.core.DataProvider;
import com.seed.core.model.ServiceResponse;
import com.seed.fund.model.ExternalFundMetaData;
import com.seed.fund.model.FundHistoricalData;

import java.time.LocalDate;
import java.util.List;

public class TefasDataProvider implements DataProvider<ExternalFundMetaData, FundHistoricalData> {
    private final MetaDataListExporter metaDataListExporter;

    public TefasDataProvider(MetaDataListExporter metaDataListExporter) {
        this.metaDataListExporter = metaDataListExporter;
    }

    @Override
    public ServiceResponse<List<ExternalFundMetaData>> exportMetaData() {
        return metaDataListExporter.export();
    }

    @Override
    public ServiceResponse<List<FundHistoricalData>> exportHistoricalData(LocalDate valueDate) {
        return null;
    }
}
