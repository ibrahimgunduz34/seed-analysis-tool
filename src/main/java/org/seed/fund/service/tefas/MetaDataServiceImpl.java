package org.seed.fund.service.tefas;

import org.seed.fund.MetaDataService;
import org.seed.fund.model.ExternalMetaData;
import org.seed.fund.model.ServiceResponse;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.List;

@Component
public class MetaDataServiceImpl implements MetaDataService {
    private final HttpClient httpClient;
    private final MetaDataListRequestBuilder requestBuilder;
    private final MetaDataListResponseParser responseParser;

    public MetaDataServiceImpl(HttpClient httpClient, MetaDataListRequestBuilder requestBuilder, MetaDataListResponseParser responseParser) {
        this.httpClient = httpClient;
        this.requestBuilder = requestBuilder;
        this.responseParser = responseParser;
    }

    @Override
    public ServiceResponse<List<ExternalMetaData>> retrieveList() {
        HttpRequest httpRequest = requestBuilder.buildRequest();
        HttpResponse<String> response;

        try {
            response = httpClient.send(httpRequest, HttpResponse.BodyHandlers.ofString());
        } catch (IOException | InterruptedException e) {
            return new ServiceResponse<>(null, e.getMessage());
        }

        List<ExternalMetaData> parsedResponse = responseParser.parse(response);

        return new ServiceResponse<>(parsedResponse, null);
    }
}
