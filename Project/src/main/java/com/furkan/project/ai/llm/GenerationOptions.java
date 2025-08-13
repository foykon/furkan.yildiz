package com.furkan.project.ai.llm;

import lombok.Data;

import java.util.Map;

@Data
public class GenerationOptions {
    private String model;
    private Double temperature;
    private Integer maxTokens;
    private Map<String, Object> extra;

    public GenerationOptions(  String model, Double temperature, Integer maxTokens) {

        this.maxTokens = maxTokens;
        this.model = model;
        this.temperature = temperature;
    }
}
