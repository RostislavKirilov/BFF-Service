package com.tinqinacademy.bff.domain;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.tinqinacademy.authentication.restexport.RestExportValidateToken;
import com.tinqinacademy.hotel.restexport.RestExportInterface;
import feign.Feign;
import feign.jackson.JacksonDecoder;
import feign.jackson.JacksonEncoder;
import feign.okhttp.OkHttpClient;
import feign.slf4j.Slf4jLogger;
import io.micrometer.core.instrument.binder.okhttp3.OkHttpContext;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@RequiredArgsConstructor
@Slf4j
public class FeignConfig {


    private final ApplicationContext applicationContext;
    private final ObjectMapper objectMapper;

    @Value("${hotel.service.url}")
    private String HOTEL_URL;


    @Bean
    public RestExportInterface restExportInterface() {


        objectMapper.findAndRegisterModules();

        return Feign.builder()
                .client(new OkHttpClient())
                .encoder(new JacksonEncoder(objectMapper))
                .decoder(new JacksonDecoder(objectMapper))
                .logger(new Slf4jLogger(RestExportInterface.class))
                .target(RestExportInterface.class, "http://localhost:8082");
    }

    @Bean
    public RestExportValidateToken restExportValidateToken() {


        objectMapper.findAndRegisterModules();

        return Feign.builder()
                .client(new OkHttpClient())
                .encoder(new JacksonEncoder(objectMapper))
                .decoder(new JacksonDecoder(objectMapper))
                .logger(new Slf4jLogger(RestExportValidateToken.class))
                .target(RestExportValidateToken.class, "http://localhost:8086");
    }
}
