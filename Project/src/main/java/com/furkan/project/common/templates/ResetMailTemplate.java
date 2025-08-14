package com.furkan.project.common.templates;

import org.springframework.stereotype.Component;

@Component
public class ResetMailTemplate {
    public String build(String username, String link) {
        return """
           <div style="font-family:Arial,sans-serif">
             <h2>Şifre Sıfırlama</h2>
             <p>Merhaba %s,</p>
             <p>Şifreni sıfırlamak için aşağıdaki bağlantıya tıkla. Bu bağlantı sınırlı süre geçerlidir.</p>
             <p><a href="%s" target="_blank">%s</a></p>
             <p>Eğer talep etmediysen, bu e-postayı dikkate alma.</p>
           </div>
        """.formatted(username, link, link);
    }
}
