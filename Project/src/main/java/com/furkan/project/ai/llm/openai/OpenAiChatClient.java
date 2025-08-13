package com.furkan.project.ai.llm.openai;

import com.furkan.project.ai.llm.GenerationOptions;
import com.furkan.project.ai.llm.LlmClient;
import com.furkan.project.ai.llm.Message;
import com.furkan.project.ai.llm.Role;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.BodyInserters;
import org.springframework.web.reactive.function.client.WebClient;

import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

@Component
public class OpenAiChatClient implements LlmClient {

    private final WebClient client;

    public OpenAiChatClient(WebClient aiWebClient) {
        this.client = aiWebClient;
    }

    @Override
    public String generateText(List<Message> messages, GenerationOptions opts) {
        Map<String, Object> body = buildPayload(messages, opts);

        String userContent = ((Map<String, Object>) ((List<?>) body.get("messages")).get(1)).get("content").toString();
        System.out.println("[AI] user content preview: " + userContent.substring(0, Math.min(120, userContent.length())));

        Map<?, ?> resp = client.post()
                .uri("/chat/completions")
                .contentType(MediaType.APPLICATION_JSON)
                .body(BodyInserters.fromValue(body))
                .retrieve()
                .bodyToMono(Map.class)
                .block();

        List<?> choices = (List<?>) resp.get("choices");
        Map<?, ?> first = (Map<?, ?>) choices.get(0);
        Map<?, ?> msg = (Map<?, ?>) first.get("message");
        var msgsList = (java.util.List<?>) body.get("messages");
        System.out.println("[AI] roles: " + ((java.util.Map<?,?>)msgsList.get(0)).get("role")
                + ", " + ((java.util.Map<?,?>)msgsList.get(1)).get("role"));
        System.out.println("[AI] sys preview: " + ((java.util.Map<?,?>)msgsList.get(0)).get("content").toString()
                .substring(0, Math.min(80, ((java.util.Map<?,?>)msgsList.get(0)).get("content").toString().length())));
        System.out.println("[AI] usr preview: " + ((java.util.Map<?,?>)msgsList.get(1)).get("content").toString()
                .substring(0, Math.min(80, ((java.util.Map<?,?>)msgsList.get(1)).get("content").toString().length())));
        return (String) msg.get("content");
    }

    @Override
    public void streamText(List<Message> messages, GenerationOptions opts,
                           java.util.function.Consumer<String> onDelta,
                           java.util.function.Consumer<Throwable> onError,
                           Runnable onComplete) {
        Map<String, Object> body = buildPayload(messages, opts);
        body.put("stream", true);

        client.post()
                .uri("/chat/completions")
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(body)
                .retrieve()
                .bodyToFlux(String.class)
                .subscribe(onDelta, onError, onComplete);
    }

    private Map<String, Object> buildPayload(List<Message> messages, GenerationOptions opts) {
        List<Map<String, Object>> msgs = messages.stream()
                .map(m -> Map.<String, Object>of(
                        "role", toOpenAiRole(m.role()),
                        "content", m.content()
                ))
                .toList();

        Map<String, Object> body = new HashMap<>();
        body.put("model", opts.getModel());
        if (opts.getTemperature() != null) body.put("temperature", opts.getTemperature());
        if (opts.getMaxTokens() != null)    body.put("max_tokens", opts.getMaxTokens());
        body.put("messages", msgs);
        return body;
    }

    private String toOpenAiRole(Role r) {
        // "system" | "user" | "assistant"
        return r.name().toLowerCase(Locale.ROOT);
    }
}
