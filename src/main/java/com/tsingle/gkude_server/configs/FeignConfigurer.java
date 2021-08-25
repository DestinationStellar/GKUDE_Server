package com.tsingle.gkude_server.configs;

import feign.codec.Decoder;
import feign.codec.Encoder;

import feign.form.spring.SpringFormEncoder;
import feign.jackson.JacksonDecoder;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.ObjectFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.http.HttpMessageConverters;
import org.springframework.cloud.openfeign.support.SpringEncoder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class FeignConfigurer {
    @NonNull
    private ObjectFactory<HttpMessageConverters> converters;

    @Bean
    Decoder feignDecoder() {
        return new JacksonDecoder();
    }

    @Bean
    Encoder feignFormEncoder() {
        return new SpringFormEncoder(new SpringEncoder(converters));
    }
}
