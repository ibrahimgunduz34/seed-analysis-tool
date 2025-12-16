package com.seed.configuration;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.net.http.HttpClient;

@Configuration
public class HttpClientConfiguration {
    @Bean
    HttpClient httpClient() {
        return HttpClient.newBuilder().build();
    }
}
