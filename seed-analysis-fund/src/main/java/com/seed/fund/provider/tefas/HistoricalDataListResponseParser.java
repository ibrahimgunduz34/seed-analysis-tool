package com.seed.fund.provider.tefas;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;
import com.seed.fund.model.ExternalFundHistoricalData;

import java.lang.reflect.Type;
import java.net.http.HttpResponse;
import java.util.List;

public class HistoricalDataListResponseParser {
    public List<ExternalFundHistoricalData> parse(HttpResponse<String> response) {
        Type typeRef = new TypeToken<List<ExternalFundHistoricalData>>() {}.getType();
        Gson gson = new GsonBuilder()
                .registerTypeAdapter(typeRef, new ExternalHistoricalDataDeserializer())
                .create();

        return gson.fromJson(response.body(), typeRef);
    }
}
