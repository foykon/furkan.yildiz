package com.furkan.project.ai.llm;

import java.util.List;
import java.util.function.Consumer;

public interface LlmClient {
    String generateText(List<Message> messages, GenerationOptions options);

    void streamText(
            List<Message> messages,
            GenerationOptions options,
            Consumer<String> onDelta,
            Consumer<Throwable> onError,
            Runnable onComplete
    );
}