package com.furkan.project.ai.controller;

import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController
@RequestMapping("/api/v1/ai/cache")
public class AiCacheController {
    private final org.springframework.cache.CacheManager cacheManager;
    public AiCacheController(org.springframework.cache.CacheManager cacheManager) { this.cacheManager = cacheManager; }

    @DeleteMapping("/comments")
    public Map<String, Object> clearComments() {
        var c = cacheManager.getCache("aiComments");
        if (c != null) c.clear();
        return java.util.Map.of("cleared", true, "cache", "aiComments");
    }
}
