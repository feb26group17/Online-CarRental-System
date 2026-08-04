package com.carrental.bookingservice.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.client.SimpleClientHttpRequestFactory;
import org.springframework.web.client.RestTemplate;

@Configuration
public class RestTemplateConfig {

    @Bean
    public RestTemplate restTemplate(@Value("${crud.service.connect-timeout:10000}") int connectTimeout,
                                     @Value("${crud.service.read-timeout:30000}") int readTimeout) {
        SimpleClientHttpRequestFactory factory = new SimpleClientHttpRequestFactory();
        factory.setConnectTimeout(connectTimeout); // 10s connection timeout
        factory.setReadTimeout(readTimeout);       // 30s read timeout
        return new RestTemplate(factory);
    }
}
