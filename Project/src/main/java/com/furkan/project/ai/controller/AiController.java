package com.furkan.project.ai.controller;

import com.furkan.project.ai.dto.AiCommentRequest;
import com.furkan.project.ai.dto.AiCommentResponse;
import com.furkan.project.ai.service.AiService;
import com.furkan.project.common.result.SuccessDataResult;
import org.springframework.http.MediaType;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

@RestController
@RequestMapping("/api/v1/movies/{movieId}/ai")
public class AiController {

    private final AiService ai;

    public AiController(AiService ai) {
        this.ai = ai;
    }

    @PostMapping("/comment")
    public SuccessDataResult<AiCommentResponse> comment(@PathVariable Long movieId,
                                                        @RequestBody(required = false) AiCommentRequest req,
                                                        Authentication auth) {
        String username = auth.getName();
        String text = ai.generateMovieComment(movieId, username, req != null ? req.getTone() : null);
        return new SuccessDataResult<>(new AiCommentResponse(movieId, username, text));
    }

    @GetMapping(value = "/comment/stream", produces = MediaType.TEXT_EVENT_STREAM_VALUE)
    public SseEmitter stream(@PathVariable Long movieId,
                             @RequestParam(required = false) String tone,
                             Authentication auth) {
        String username = auth.getName();
        return ai.streamMovieComment(movieId, username, tone);
    }

}
