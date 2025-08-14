package com.furkan.project.auth.events;

import com.furkan.project.common.service.EmailService;
import com.furkan.project.common.service.MessageService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;
import org.springframework.transaction.event.TransactionPhase;
import org.springframework.transaction.event.TransactionalEventListener;
import com.furkan.project.common.templates.ResetMailTemplate;

@Slf4j
@Component
@RequiredArgsConstructor
public class ResetPasswordEmailListener {

    private final EmailService emailService;
    private final ResetMailTemplate template;
    private final MessageService messages;

    @Async
    @TransactionalEventListener(phase = TransactionPhase.AFTER_COMMIT)
    public void on(ResetPasswordEmailEvent e) {
        try {
            String body = template.build(e.username(), e.link());
            emailService.send(e.to(), messages.get("mail.reset.subject"), body);
            log.info("Reset mail dispatched userId={} email={}", e.userId(), e.to());
        } catch (Exception ex) {
            log.warn("Reset mail FAILED userId={} email={} err={}", e.userId(), e.to(), ex.toString());
        }
    }
}
