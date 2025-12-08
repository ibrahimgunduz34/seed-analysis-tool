package com.seed.fund.provider;

import com.seed.core.model.ServiceResponse;
import com.seed.fund.model.ExternalFundMetaData;
import com.seed.fund.provider.tefas.MetaDataListRequestBuilder;
import com.seed.fund.provider.tefas.MetaDataListResponseParser;

import java.io.IOException;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.List;

public class MetaDataListExporter {
    private final HttpClient httpClient;
    private final MetaDataListRequestBuilder requestBuilder;
    private final MetaDataListResponseParser responseParser;

    public MetaDataListExporter(HttpClient httpClient,
                                MetaDataListRequestBuilder requestBuilder,
                                MetaDataListResponseParser responseParser) {
        this.httpClient = httpClient;
        this.requestBuilder = requestBuilder;
        this.responseParser = responseParser;
    }

    public ServiceResponse<List<ExternalFundMetaData>> export() {
        HttpRequest httpRequest = requestBuilder.buildRequest();
        HttpResponse<String> response;

        try {
            response = httpClient.send(httpRequest, HttpResponse.BodyHandlers.ofString());
        } catch (IOException | InterruptedException e) {
            return new ServiceResponse<>(null, e.getMessage());
        }

        List<ExternalFundMetaData> parsedResponse = responseParser.parse(response);

        return new ServiceResponse<>(parsedResponse, null);
    }
}
