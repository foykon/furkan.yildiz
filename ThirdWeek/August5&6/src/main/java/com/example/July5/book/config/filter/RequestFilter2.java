package com.example.July5.book.config.filter;

import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.log4j.Log4j2;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.CommonsRequestLoggingFilter;

@Component
@Log4j2
@Order(1)
public class RequestFilter2 extends CommonsRequestLoggingFilter {
    @Override
    protected boolean shouldLog(HttpServletRequest request){
        return request.getRequestURI().contains("books");
    }

    @Override
    protected void afterRequest(HttpServletRequest request, String message) {
        log.info("After Request {} {}", request.getRequestURI(), message);
    }

    @Override
    protected void beforeRequest(HttpServletRequest request, String message) {
        log.info("Before Request {} {}", request.getRequestURI(), message);

    }
}
