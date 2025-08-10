package com.furkan.project.auth.service;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.time.Duration;

public interface RefreshCookieService {
    void set(HttpServletResponse res, String raw, Duration ttl);
    void clear(HttpServletResponse res);
    String read(HttpServletRequest req);
}