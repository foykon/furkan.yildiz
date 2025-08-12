package com.furkan.project.common.dto;

public record FieldErrorDetail(
        String field,
        Object rejectedValue,
        String message
) {}
