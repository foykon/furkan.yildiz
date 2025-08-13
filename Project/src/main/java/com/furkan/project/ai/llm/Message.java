package com.furkan.project.ai.llm;

import lombok.Data;

public record Message(Role role, String content) {
    public static Message system(String content) { return new Message(Role.SYSTEM, content); }
    public static Message user(String content)    { return new Message(Role.USER, content); }
    public static Message assistant(String content){ return new Message(Role.ASSISTANT, content); }
}