package com.parksupark.soomjae.server.common.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.parksupark.soomjae.server.common.filter.RequestLoggingFilter;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@RequiredArgsConstructor
public class FilterConfig {

    private final ObjectMapper objectMapper;

    @Bean
    public FilterRegistrationBean<RequestLoggingFilter> requestLoggingFilter() {
        FilterRegistrationBean<RequestLoggingFilter> bean = new FilterRegistrationBean<>();

        bean.setFilter(new RequestLoggingFilter(objectMapper));
        bean.addUrlPatterns("/*");
        bean.setOrder(1);
        bean.setName("RequestLoggingFilter");
        return bean;
    }

}
