package com.furkan.project.ai.service;


import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

public interface AiService {
    String generateMovieComment(Long movieId, String username, String tone);
    SseEmitter streamMovieComment(Long movieId, String username, String tone);
}
