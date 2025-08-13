package com.furkan.project.ai.dto;

public class AiCommentRequest {
    private String tone;

    public AiCommentRequest() {}
    public AiCommentRequest(String tone) { this.tone = tone; }

    public String getTone() { return tone; }
    public void setTone(String tone) { this.tone = tone; }
}

