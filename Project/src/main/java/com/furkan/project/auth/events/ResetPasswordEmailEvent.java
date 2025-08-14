package com.furkan.project.auth.events;

public record ResetPasswordEmailEvent(Long userId, String to, String username, String link) {}
