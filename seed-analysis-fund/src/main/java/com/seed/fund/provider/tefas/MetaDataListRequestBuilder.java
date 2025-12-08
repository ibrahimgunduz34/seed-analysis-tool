package com.seed.fund.provider.tefas;

import java.net.URI;
import java.net.URLEncoder;
import java.net.http.HttpRequest;
import java.nio.charset.StandardCharsets;
import java.time.Duration;
import java.util.HashMap;
import java.util.Map;


public class MetaDataListRequestBuilder {
    private static final String SERVICE_URL = "https://www.tefas.gov.tr/api/DB/BindComparisonFundReturns";
    private static final Long CONNECTION_TIMEOUT = 30_000L;

    public HttpRequest buildRequest() {
        Map<String, String> formData = new HashMap<>() {{
            put("calismatipi", "2");
            put("fontip", "YAT");
            put("sfontur", "");
            put("kurucukod", "" );
            put("fongrup", "");
            put("bastarih", "Başlangıç");
            put("bittarih", "Bitiş");
            put("fonturkod", "");
            put("fonunvantip", "");
            put("strperiod", "1,1,1,1,1,1,1");
            put("islemdurum", ""); // Tum Fonlar (Tefas'da islem gorenler ve digerleri)
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
            if (!formBodyBuilder.isEmpty()) {
                formBodyBuilder.append("&");
            }
            formBodyBuilder.append(URLEncoder.encode(singleEntry.getKey(), StandardCharsets.UTF_8));
            formBodyBuilder.append("=");
            formBodyBuilder.append(URLEncoder.encode(singleEntry.getValue(), StandardCharsets.UTF_8));
        }
        return formBodyBuilder.toString();
    }
}
