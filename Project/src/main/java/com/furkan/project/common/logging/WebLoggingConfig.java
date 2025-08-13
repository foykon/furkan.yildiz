package com.furkan.project.common.logging;

import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.Ordered;

@Configuration
public class WebLoggingConfig {

    @Bean
    public FilterRegistrationBean<RequestLogFilter> requestLogFilterRegistration() {
        FilterRegistrationBean<RequestLogFilter> reg = new FilterRegistrationBean<>();
        reg.setFilter(new RequestLogFilter());
        reg.setOrder(Ordered.LOWEST_PRECEDENCE);
        return reg;
    }
}
