package com.furkan.project.ai.service.impl;

import com.furkan.project.ai.config.AiProperties;
import com.furkan.project.ai.llm.GenerationOptions;
import com.furkan.project.ai.llm.LlmClient;
import com.furkan.project.ai.llm.Message;
import com.furkan.project.ai.llm.Role;
import com.furkan.project.ai.prompt.PromptTemplates;
import com.furkan.project.ai.service.AiService;
import com.furkan.project.movie.api.MovieApiService;
import com.furkan.project.movie.api.MovieSummaryForAi;
import io.github.resilience4j.ratelimiter.annotation.RateLimiter;
import io.github.resilience4j.retry.annotation.Retry;
import lombok.extern.log4j.Log4j2;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CompletableFuture;

@Log4j2
@Service
public class AiServiceImpl implements AiService {

    private final LlmClient llm;
    private final AiProperties props;
    private final MovieApiService movieApi;

    public AiServiceImpl(LlmClient llm, AiProperties props, MovieApiService movieApi) {
        this.llm = llm;
        this.props = props;
        this.movieApi = movieApi;
    }

    @Override
    @Retry(name = "ai")
    @RateLimiter(name = "ai")
    @Cacheable(value = "aiComments",
            key = "T(String).valueOf(#movieId).concat('|').concat(#username).concat('|').concat(#tone==null?'':#tone)")
    public String generateMovieComment(Long movieId, String username, String tone) {
        MovieSummaryForAi m = movieApi.getSummaryForAi(movieId);
        List<Message> messages = buildMessages(m, username, tone);
        GenerationOptions opts = new GenerationOptions(  props.getModel(), props.getTemperature(), props.getMaxTokens());

        return llm.generateText(messages, opts);
    }

    @Override
    @Retry(name = "ai")
    @RateLimiter(name = "ai")
    public SseEmitter streamMovieComment(Long movieId, String username, String tone) {
        MovieSummaryForAi m = movieApi.getSummaryForAi(movieId);
        List<Message> messages = buildMessages(m, username, tone);
        GenerationOptions opts = new GenerationOptions(  props.getModel(), props.getTemperature(),props.getMaxTokens());

        SseEmitter emitter = new SseEmitter(0L);
        CompletableFuture.runAsync(() -> llm.streamText(
                messages, opts,
                delta -> trySend(emitter, delta),
                err -> tryCompleteWithError(emitter, err),
                emitter::complete
        ));
        return emitter;
    }

    // AiServiceImpl (veya bir helper sınıfta)
    private List<Message> buildMessages(MovieSummaryForAi m, String username, String tone) {
        String sys = PromptTemplates.movieSystemPrompt();

        String release = m.getReleaseDate() != null ? m.getReleaseDate().toString() : "";
        String genres  = (m.getGenres() == null || m.getGenres().isEmpty())
                ? "" : String.join(", ", m.getGenres());

        String usr = PromptTemplates.userPrompt(
                username,
                m.getTitle(),
                release,
                genres,
                m.getDirectorName(),
                m.getRating(),
                m.getDescription(),
                tone
        );

        return java.util.List.of(
                Message.system(sys),
                Message.user(usr)
        );
    }


    private static String nullToNA(String s) { return (s == null || s.isBlank()) ? "N/A" : s; }

    private static void trySend(SseEmitter emitter, String data) {
        try { emitter.send(SseEmitter.event().data(data).name("delta").reconnectTime(1500).data(data, org.springframework.http.MediaType.TEXT_PLAIN)
); }
        catch (IOException ignored) {}
    }
    private static void tryCompleteWithError(SseEmitter emitter, Throwable e) {
        try { emitter.completeWithError(e); } catch (IllegalStateException ignored) {}
    }
}
