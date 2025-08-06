package com.furkan.project.auth.exception;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.Date;

class ErrorResponse {
    private int status;
    private LocalDateTime date;
    private String error;
    private String message;

    public ErrorResponse(int status, LocalDateTime date, String error, String message) {
        this.status = status;
        this.date = date;
        this.error = error;
        this.message = message;
    }
}
