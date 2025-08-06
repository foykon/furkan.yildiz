package com.furkan.project.common.service;

public interface MessageService {
    String get(String key);
    String get(String key, Object... args);
}
