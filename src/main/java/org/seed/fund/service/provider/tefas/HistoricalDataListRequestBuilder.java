package org.seed.fund.service.provider.tefas;

import org.springframework.stereotype.Component;

import java.net.URI;
import java.net.URLEncoder;
import java.net.http.HttpRequest;
import java.nio.charset.StandardCharsets;
import java.time.Duration;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.Map;

@Component
public class HistoricalDataListRequestBuilder {
    private static final String SERVICE_URL = "https://www.tefas.gov.tr/api/DB/BindHistoryInfo";
    private static final Long CONNECTION_TIMEOUT = 30_000L;

    public HttpRequest buildRequest(LocalDate valueDate) {
        String formattedValueDate = valueDate.format(DateTimeFormatter.ofPattern("dd.MM.yyyy"));

        Map<String, String> formData = new HashMap<>() {{
            put("fontip", "YAT");
            put("sfontur", "");
            put("fonkod", "");
            put("fongrup", "");
            put("bastarih", formattedValueDate);
            put("bittarih", formattedValueDate);
            put("fonturkod", "");
            put("fonunvantip", "");
        }};

        return HttpRequest.newBuilder()
                .uri(URI.create(SERVICE_URL))
                .header("accept", "application/json, text/javascript, */*; q=0.01")
                .header("accept-language", "tr-TR,tr;q=0.9,en-US;q=0.8,en;q=0.7,ro;q=0.6")
                .header("content-type", "application/x-www-form-urlencoded; charset=UTF-8")
                .header("sec-ch-ua", "\"Not/A)Brand\";v=\"99\", \"Google Chrome\";v=\"115\", \"Chromium\";v=\"115\"")
                .header("sec-ch-ua-mobile", "?0")
                .header("sec-ch-ua-platform", "\"Linux\"")
                .header("sec-fetch-dest", "empty")
                .header("sec-fetch-mode", "cors")
                .header("sec-fetch-site", "same-origin")
                .header("x-requested-with", "XMLHttpRequest")
                .POST(HttpRequest.BodyPublishers.ofString(getFormDataAsString(formData)))
                .timeout(Duration.ofMillis(CONNECTION_TIMEOUT))
                .build();

    }

    private static String getFormDataAsString(Map<String, String> formData) {
        StringBuilder formBodyBuilder = new StringBuilder();
        for (Map.Entry<String, String> singleEntry : formData.entrySet()) {
            if (formBodyBuilder.length() > 0) {
                formBodyBuilder.append("&");
            }
            formBodyBuilder.append(URLEncoder.encode(singleEntry.getKey(), StandardCharsets.UTF_8));
            formBodyBuilder.append("=");
            formBodyBuilder.append(URLEncoder.encode(singleEntry.getValue(), StandardCharsets.UTF_8));
        }
        return formBodyBuilder.toString();
    }


}
