package org.seed.fund.service.tefas;

import com.google.gson.*;
import org.seed.fund.model.ExternalMetaData;

import java.lang.reflect.Type;
import java.util.Currency;
import java.util.List;

public class ExternalMetaDataDeserializer implements JsonDeserializer<List<ExternalMetaData>> {
    @Override
    public List<ExternalMetaData> deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) throws JsonParseException {
        JsonObject obj = json.getAsJsonObject();
        if (!obj.has("data")) {
            throw new IllegalArgumentException("Received invalid response from TEFAS");
        }

        JsonElement data = obj.get("data");

        return data.getAsJsonArray()
                .asList()
                .stream()
                .map(e -> {
                    JsonObject item = e.getAsJsonObject();
                    return new ExternalMetaData(
                            item.get("FONKODU").getAsString(),
                            item.get("FONUNVAN").getAsString(),
                            item.get("FONTURACIKLAMA").getAsString(),
                            Currency.getInstance("TRY")
                    );
                })
                .toList();
    }
}
