package com.seed.fund.provider.tefas;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;
import com.seed.fund.model.ExternalFundMetaData;
import org.springframework.stereotype.Component;

import java.lang.reflect.Type;
import java.net.http.HttpResponse;
import java.util.List;

@Component
public class MetaDataListResponseParser {
    public List<ExternalFundMetaData> parse(HttpResponse<String> response) {
        Type typeRef = new TypeToken<List<ExternalFundMetaData>>() {}.getType();
        Gson gson = new GsonBuilder()
                .registerTypeAdapter(typeRef, new ExternalMetaDataDeserializer())
                .create();

        return gson.fromJson(response.body(), typeRef);
    }
}
