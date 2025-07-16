package com.project.nyang.global.common.publicapi;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestTemplate;

/**
 * RestTemplateConfig입니다.
 *
 * @author : 엄아영
 * @fileName : RestTemplateConfig
 * @since : 2025-07-14
 */
@Configuration
public class RestTemplateConfig {
    @Bean
    public RestTemplate restTemplate() {
        return new RestTemplate();
    }
}