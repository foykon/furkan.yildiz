package com.furkan.project.auth.service.impl;

import com.furkan.project.auth.service.RefreshCookieService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.time.Duration;

@Service
public class RefreshCookieServiceImpl implements RefreshCookieService {
    private static final String NAME = "REFRESH_TOKEN";
    @Value("${app.refresh.cookie-secure:false}") private boolean secure;
    @Value("${app.refresh.cookie-path:/api/auth}") private String path;
    @Value("${app.refresh.cookie-samesite:Lax}") private String sameSite;

    @Override
    public void set(HttpServletResponse res, String raw, Duration ttl) {
        int maxAge = (int) ttl.getSeconds();
        res.addHeader("Set-Cookie",
                NAME + "=" + raw +
                        "; Max-Age=" + maxAge +
                        "; Path=" + path +
                        (secure ? "; Secure" : "") +
                        "; HttpOnly; SameSite=" + sameSite);
    }
    @Override
    public void clear(HttpServletResponse res) {
        res.addHeader("Set-Cookie",
                NAME + "=; Max-Age=0; Path=" + path +
                        (secure ? "; Secure" : "") +
                        "; HttpOnly; SameSite=" + sameSite);
    }
    @Override
    public String read(HttpServletRequest req) {
        var cookies = req.getCookies();
        if (cookies == null) return null;
        for (var c : cookies) if (NAME.equals(c.getName())) return c.getValue();
        return null;
    }
}
