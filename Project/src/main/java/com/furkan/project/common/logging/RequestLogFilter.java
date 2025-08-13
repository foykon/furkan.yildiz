package com.furkan.project.common.logging;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.filter.OncePerRequestFilter;
import java.io.IOException;

public class RequestLogFilter extends OncePerRequestFilter {
    private static final Logger log = LoggerFactory.getLogger(RequestLogFilter.class);

    @Value("${app.logging.http.slow-threshold-ms:1000}")
    private long slowThresholdMs;

    @Override
    protected boolean shouldNotFilter(HttpServletRequest request) {
        String p = request.getRequestURI();
        return "/actuator/health".equals(p);
    }

    @Override
    protected void doFilterInternal(
            HttpServletRequest req,
            HttpServletResponse res,
            FilterChain chain) throws ServletException, IOException {

        long t0 = System.nanoTime();
        try {
            chain.doFilter(req, res);
        } finally {
            long ms = (System.nanoTime() - t0) / 1_000_000;
            int status = res.getStatus();
            String m = req.getMethod();
            String q = req.getQueryString();
            String path = (q == null) ? req.getRequestURI() : req.getRequestURI() + "?" + q;

            if (status >= 500 || ms >= slowThresholdMs) {
                log.warn("http request method={} path={} status={} durationMs={}", m, path, status, ms);
            } else {
                log.info("http request method={} path={} status={} durationMs={}", m, path, status, ms);
            }
        }
    }
}