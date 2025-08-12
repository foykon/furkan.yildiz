package com.furkan.project.common.dto;

import com.furkan.project.common.entity.ErrorCode;

import java.time.Instant;
import java.util.List;

public record ApiErrorPayload(
        ErrorCode code,
        String path,
        Instant timestamp,
        List<FieldErrorDetail> fieldErrors
) {}