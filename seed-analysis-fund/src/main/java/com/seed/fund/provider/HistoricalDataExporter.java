package com.seed.fund.provider;

import com.seed.core.model.ServiceResponse;
import com.seed.fund.model.ExternalFundHistoricalData;
import com.seed.fund.provider.tefas.HistoricalDataListRequestBuilder;
import com.seed.fund.provider.tefas.HistoricalDataListResponseParser;

import java.io.IOException;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.time.LocalDate;
import java.util.List;

public class HistoricalDataExporter {
    private final HttpClient httpClient;
    private final HistoricalDataListRequestBuilder requestBuilder;
    private final HistoricalDataListResponseParser responseParser;

    public HistoricalDataExporter(HttpClient httpClient,
                                  HistoricalDataListRequestBuilder requestBuilder,
                                  HistoricalDataListResponseParser responseParser) {
        this.httpClient = httpClient;
        this.requestBuilder = requestBuilder;
        this.responseParser = responseParser;
    }

    public ServiceResponse<List<ExternalFundHistoricalData>> export(LocalDate valueDate) {
        HttpRequest httpRequest = requestBuilder.buildRequest(valueDate);
        HttpResponse<String> response;

        try {
            response = httpClient.send(httpRequest, HttpResponse.BodyHandlers.ofString());
        } catch (IOException | InterruptedException e) {
            return new ServiceResponse<>(null, e.getMessage());
        }
        List<ExternalFundHistoricalData> parsedResponse = responseParser.parse(response);

        return new ServiceResponse<>(parsedResponse, null);
    }
}
