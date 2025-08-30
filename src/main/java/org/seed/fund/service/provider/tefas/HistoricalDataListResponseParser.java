package org.seed.fund.service.provider.tefas;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;
import org.seed.fund.model.ExternalHistoricalData;
import org.seed.fund.model.ExternalMetaData;
import org.seed.fund.model.MetaData;
import org.springframework.stereotype.Component;

import java.lang.reflect.Type;
import java.net.http.HttpResponse;
import java.util.List;

@Component
public class HistoricalDataListResponseParser {
    public List<ExternalHistoricalData> parse(HttpResponse<String> response, List<MetaData> availableMetaData) {
        Type typeRef = new TypeToken<List<ExternalMetaData>>() {}.getType();
        Gson gson = new GsonBuilder()
                .registerTypeAdapter(typeRef, new ExternalHistoricalDataDeserializer(availableMetaData))
                .create();

        return gson.fromJson(response.body(), typeRef);
    }
}
