package com.example.July5.book.config.interceptor;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import java.util.Date;

@Slf4j
@Component
public class RequestInterceptor implements HandlerInterceptor {

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        request.setAttribute("startDate", new Date());
        log.info("RequestInterceptor preHandle {} {}", request.getRequestURI(), request.getMethod());
        return HandlerInterceptor.super.preHandle(request, response, handler);
    }

    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) throws Exception {
        log.info("RequestInterceptor after compliton {} {}ms", request.getRequestURI(), request.getMethod());
    }

    @Override
    public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler, ModelAndView modelAndView) throws Exception {
        Date startDate= (Date) request.getAttribute("startDate");
        Date endDate = new Date();
        long diff = endDate.getTime() - startDate.getTime();
        log.info("RequestInterceptor post handle {} {} {}ms", request.getRequestURI(), request.getMethod(), diff);
    }
}
