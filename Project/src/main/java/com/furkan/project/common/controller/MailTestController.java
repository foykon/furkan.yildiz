package com.furkan.project.common.controller;

import com.furkan.project.common.service.EmailService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/ops/mail")
@RequiredArgsConstructor
public class MailTestController {

    private final EmailService email;

    @PostMapping("/test")
    public String test(@RequestParam String to) {
        String html = """
            <h3>Mail testi</h3>
            <p>Bu bir test mesajıdır.</p>
        """;
        email.send(to, "SMTP Test", html);
        return "OK (loglara bak: eğer hata yoksa gönderildi)";
    }
}