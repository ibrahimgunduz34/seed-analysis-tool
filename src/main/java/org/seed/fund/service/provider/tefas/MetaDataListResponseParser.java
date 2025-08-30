package org.seed.fund.service.provider.tefas;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;
import org.seed.fund.model.ExternalMetaData;
import org.springframework.stereotype.Component;

import java.lang.reflect.Type;
import java.net.http.HttpResponse;
import java.util.List;

@Component
public class MetaDataListResponseParser {
    public List<ExternalMetaData> parse(HttpResponse<String> response) {
        Type typeRef = new TypeToken<List<ExternalMetaData>>() {}.getType();
        Gson gson = new GsonBuilder()
                .registerTypeAdapter(typeRef, new ExternalMetaDataDeserializer())
                .create();

        return gson.fromJson(response.body(), typeRef);
    }
}
