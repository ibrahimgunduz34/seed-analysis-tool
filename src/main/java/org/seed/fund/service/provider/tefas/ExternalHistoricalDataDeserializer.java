package org.seed.fund.service.provider.tefas;

import com.google.gson.*;
import org.seed.fund.model.ExternalFundHistoricalData;
import org.seed.fund.model.FundMetaData;

import java.lang.reflect.Type;
import java.math.RoundingMode;
import java.time.Instant;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

public class ExternalHistoricalDataDeserializer implements JsonDeserializer<List<ExternalFundHistoricalData>> {
    private final List<FundMetaData> availableFundMetaDataList;

    public ExternalHistoricalDataDeserializer(List<FundMetaData> availableFundMetaDataList) {
        this.availableFundMetaDataList = availableFundMetaDataList;
    }

    @Override
    public List<ExternalFundHistoricalData> deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) throws JsonParseException {
        // TODO: Refactoring required!
        // Find a better / efficient solution to generate List<FundPrice>

        JsonObject obj = json.getAsJsonObject();
        if (!obj.has("data")) {
            throw new IllegalArgumentException("Received invalid response from TEFAS");
        }

        JsonElement data = obj.get("data");

        Map<String, FundMetaData> fundsMap = availableFundMetaDataList.stream()
                .collect(Collectors.toMap(FundMetaData::getCode, Function.identity()));

        return data.getAsJsonArray()
                .asList()
                .stream()
                .filter(e -> {
                    JsonObject item = e.getAsJsonObject();
                    return fundsMap.containsKey(item.get("FONKODU").getAsString());
                })
                .map(e -> {
                    JsonObject item = e.getAsJsonObject();

                    LocalDate valueDate = Instant
                            .ofEpochMilli(item.get("TARIH").getAsLong())
                            .atZone(ZoneId.systemDefault())
                            .toLocalDate();

                    return new ExternalFundHistoricalData(
                            fundsMap.get(item.get("FONKODU").getAsString()),
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
