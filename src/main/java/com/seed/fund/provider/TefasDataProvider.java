package com.seed.fund.provider;

import com.seed.core.DataProvider;
import com.seed.core.model.ServiceResponse;
import com.seed.fund.model.ExternalFundHistoricalData;
import com.seed.fund.model.ExternalFundMetaData;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.util.List;

@Component
public class TefasDataProvider implements DataProvider<ExternalFundMetaData, ExternalFundHistoricalData> {
    private final MetaDataListExporter metaDataListExporter;
    private final HistoricalDataExporter historicalDataExporter;

    public TefasDataProvider(MetaDataListExporter metaDataListExporter, HistoricalDataExporter historicalDataExporter) {
        this.metaDataListExporter = metaDataListExporter;
        this.historicalDataExporter = historicalDataExporter;
    }

    @Override
    public ServiceResponse<List<ExternalFundMetaData>> exportMetaData() {
        return metaDataListExporter.export();
    }

    @Override
    public ServiceResponse<List<ExternalFundHistoricalData>> exportHistoricalData(LocalDate valueDate) {
        return historicalDataExporter.export(valueDate);
    }
}
