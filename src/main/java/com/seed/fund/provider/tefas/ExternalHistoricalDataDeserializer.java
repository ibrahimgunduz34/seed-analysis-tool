package com.seed.fund.provider.tefas;

import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;
import com.seed.fund.model.ExternalFundHistoricalData;

import java.lang.reflect.Type;
import java.math.RoundingMode;
import java.time.Instant;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.List;

public class ExternalHistoricalDataDeserializer implements JsonDeserializer<List<ExternalFundHistoricalData>> {

    @Override
    public List<ExternalFundHistoricalData> deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) throws JsonParseException {
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

                    LocalDate valueDate = Instant
                            .ofEpochMilli(item.get("TARIH").getAsLong())
                            .atZone(ZoneId.systemDefault())
                            .toLocalDate();

                    return new ExternalFundHistoricalData(
                            item.get("FONKODU").getAsString(),
                            item.get("TEDPAYSAYISI").getAsBigDecimal(),
                            item.get("KISISAYISI").getAsInt(),
                            item.get("PORTFOYBUYUKLUK").getAsBigDecimal(),
                            item.get("FIYAT").getAsBigDecimal().setScale(4, RoundingMode.HALF_UP),
                            valueDate
                    );
                })
                .toList();


    }
}
