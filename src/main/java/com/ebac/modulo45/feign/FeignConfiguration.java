package com.ebac.modulo45.feign;

import com.fasterxml.jackson.databind.ObjectMapper;
import feign.Feign;
import feign.jackson.JacksonDecoder;
import feign.jackson.JacksonEncoder;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;

@Configuration
@PropertySource("classpath:application.properties")
public class FeignConfiguration {

    @Value("${api.url}")
    private String apiUrl;

    private final ObjectMapper mapper;

    public FeignConfiguration() {
        mapper = new ObjectMapper();
        mapper.findAndRegisterModules();
    }

    @Bean(name = "feignApiClient")
    FeignApiClient getUserClient() {
        return Feign.builder()
                .encoder(new JacksonEncoder())
                .decoder(new JacksonDecoder())
                .target(FeignApiClient.class, apiUrl);
    }
}
