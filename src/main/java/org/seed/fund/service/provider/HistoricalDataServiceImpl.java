package org.seed.fund.service.provider;

import org.seed.fund.model.ExternalHistoricalData;
import org.seed.fund.model.MetaData;
import org.seed.fund.model.ServiceResponse;
import org.seed.fund.service.provider.tefas.HistoricalDataListRequestBuilder;
import org.seed.fund.service.provider.tefas.HistoricalDataListResponseParser;
import org.seed.fund.storage.FundStorage;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.time.LocalDate;
import java.util.List;

@Component
public class HistoricalDataServiceImpl implements HistoricalDataService {
    private final HttpClient httpClient;
    private final HistoricalDataListRequestBuilder requestBuilder;
    private final HistoricalDataListResponseParser responseParser;
    private final FundStorage fundStorage;

    public HistoricalDataServiceImpl(HttpClient httpClient, HistoricalDataListRequestBuilder requestBuilder, HistoricalDataListResponseParser responseParser, FundStorage fundStorage) {
        this.httpClient = httpClient;
        this.requestBuilder = requestBuilder;
        this.responseParser = responseParser;
        this.fundStorage = fundStorage;
    }

    @Override
    public ServiceResponse<List<ExternalHistoricalData>> retrieveList(LocalDate valueDate) {
        HttpRequest httpRequest = requestBuilder.buildRequest(valueDate);
        HttpResponse<String> response;

        try {
            response = httpClient.send(httpRequest, HttpResponse.BodyHandlers.ofString());
        } catch (IOException | InterruptedException e) {
            return new ServiceResponse<>(null, e.getMessage());
        }

        List<MetaData> availableMetaDataList = fundStorage.getMetaDataList();
        List<ExternalHistoricalData> parsedResponse = responseParser.parse(response, availableMetaDataList);

        return new ServiceResponse<>(parsedResponse, null);
    }
}
