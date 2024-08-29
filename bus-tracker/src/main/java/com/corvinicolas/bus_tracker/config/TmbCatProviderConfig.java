package com.corvinicolas.bus_tracker.config;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.MediaType;
import org.springframework.web.reactive.function.client.WebClient;

@ConfigurationProperties(prefix = "tmb.cat")
@Configuration
@Getter
@Setter
public class TmbCatProviderConfig {
    private String appId;
    private String appKey;
    private String baseUrl;
    private RequestParameters requestParameters;
    public static final String APP_ID_PARAM = "app_id";
    public static final String APP_KEY_PARAM = "app_key";

    @Getter
    @Setter
    @NoArgsConstructor
    public static class RequestParameters{
        private String stopCode;
        private String line;
    }

    @Bean
    public WebClient tmbCatWebClient() {
        return WebClient.builder()
                .defaultHeaders(httpHeaders -> {
                    httpHeaders.setContentType(MediaType.APPLICATION_JSON);
                    httpHeaders.set("User-Agent", "test");
                })
                .baseUrl(baseUrl)
                .build();
    }
}