package com.furkan.project.common.exception;

import com.furkan.project.common.result.ErrorDataResult;
import com.furkan.project.common.result.ErrorResult;
import com.furkan.project.common.result.Result;
import jakarta.persistence.EntityNotFoundException;
import jakarta.validation.ConstraintViolationException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.*;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.validation.FieldError;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;
import org.springframework.web.server.ResponseStatusException;

import java.util.*;

@Slf4j
@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<Result> handleIllegalArgument(IllegalArgumentException ex) {
        log.warn("IllegalArgument: {}", ex.getMessage());
        return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                .body(new ErrorResult(messageOrDefault(ex.getMessage(), "bad.request")));
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ErrorDataResult<Map<String, String>>> handleBeanValidation(MethodArgumentNotValidException ex) {
        Map<String, String> errors = new LinkedHashMap<>();
        for (FieldError fe : ex.getBindingResult().getFieldErrors()) {
            errors.put(fe.getField(), fe.getDefaultMessage());
        }
        log.warn("BeanValidation failed: {}", errors);
        return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                .body(new ErrorDataResult<>(errors, "validation.failed"));
    }

    @ExceptionHandler(ConstraintViolationException.class)
    public ResponseEntity<ErrorDataResult<Map<String, String>>> handleConstraint(ConstraintViolationException ex) {
        Map<String, String> errors = new LinkedHashMap<>();
        ex.getConstraintViolations().forEach(v ->
                errors.put(v.getPropertyPath().toString(), v.getMessage()));
        log.warn("ConstraintViolation: {}", errors);
        return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                .body(new ErrorDataResult<>(errors, "validation.failed"));
    }

    @ExceptionHandler(MissingServletRequestParameterException.class)
    public ResponseEntity<Result> handleMissingParam(MissingServletRequestParameterException ex) {
        log.warn("Missing param: {}", ex.getParameterName());
        return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                .body(new ErrorResult("missing.parameter." + ex.getParameterName()));
    }

    @ExceptionHandler(HttpMessageNotReadableException.class)
    public ResponseEntity<Result> handleNotReadable(HttpMessageNotReadableException ex) {
        log.warn("Malformed JSON: {}", ex.getMostSpecificCause().getMessage());
        return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                .body(new ErrorResult("malformed.json"));
    }

    // 404 - Bulunamadı

    @ExceptionHandler(EntityNotFoundException.class)
    public ResponseEntity<Result> handleEntityNotFound(EntityNotFoundException ex) {
        log.warn("EntityNotFound: {}", ex.getMessage());
        return ResponseEntity.status(HttpStatus.NOT_FOUND)
                .body(new ErrorResult(messageOrDefault(ex.getMessage(), "resource.notfound")));
    }

    // 409 - Çakışma / bütünlük hataları

    @ExceptionHandler(DataIntegrityViolationException.class)
    public ResponseEntity<Result> handleDataIntegrity(DataIntegrityViolationException ex) {
        String code = resolveConflictCode(ex);
        log.warn("DataIntegrityViolation [{}]: {}", code, ex.getMostSpecificCause().getMessage());
        return ResponseEntity.status(HttpStatus.CONFLICT)
                .header("X-Error-Code", code)
                .body(new ErrorResult(code));
    }

    private String resolveConflictCode(DataIntegrityViolationException ex) {
        Throwable cause = ex.getCause();
        if (cause instanceof org.hibernate.exception.ConstraintViolationException h) {
            String c = Optional.ofNullable(h.getConstraintName()).orElse("").toLowerCase();
            if (c.contains("uk_user_email") || c.contains("uq_user_email")) return "email.taken";
            if (c.contains("uk_user_username") || c.contains("uq_user_username")) return "username.taken";
            if (c.contains("uq_user_list_movie") || c.contains("uk_user_movie")) return "movie.already.in.list";
        }
        String msg = Optional.ofNullable(ex.getMostSpecificCause().getMessage()).orElse("").toLowerCase();
        if (msg.contains("email")) return "email.taken";
        if (msg.contains("username")) return "username.taken";
        return "data.integrity.violation";
    }

    // 500 - Beklenmeyen

    @ExceptionHandler(Exception.class)
    public ResponseEntity<Result> handleGeneric(Exception ex) {
        log.error("Unexpected error", ex);
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(new ErrorResult("internal.error"));
    }

    @ExceptionHandler(BadCredentialsException.class) // 401
    public ResponseEntity<Result> handleBadCredentials(BadCredentialsException ex) {
        log.warn("BadCredentials");
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                .body(new ErrorResult("auth.invalid.credentials"));
    }

    @ExceptionHandler(AccessDeniedException.class) // 403
    public ResponseEntity<Result> handleAccessDenied(AccessDeniedException ex) {
        log.warn("AccessDenied");
        return ResponseEntity.status(HttpStatus.FORBIDDEN)
                .body(new ErrorResult("auth.forbidden"));
    }

    @ExceptionHandler(HttpRequestMethodNotSupportedException.class) // 405
    public ResponseEntity<Result> handleMethodNotAllowed(HttpRequestMethodNotSupportedException ex) {
        log.warn("MethodNotAllowed: {}", ex.getMethod());
        return ResponseEntity.status(HttpStatus.METHOD_NOT_ALLOWED)
                .body(new ErrorResult("method.not.allowed"));
    }

    @ExceptionHandler(MethodArgumentTypeMismatchException.class) // 400 - tip/format hatası
    public ResponseEntity<Result> handleTypeMismatch(MethodArgumentTypeMismatchException ex) {
        log.warn("TypeMismatch: param={}, value={}", ex.getName(), ex.getValue());
        return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                .body(new ErrorResult("parameter.type.mismatch." + ex.getName()));
    }

    @ExceptionHandler(ResponseStatusException.class) // @ResponseStatus atılan yerler için
    public ResponseEntity<Result> handleResponseStatus(ResponseStatusException ex) {
        log.warn("ResponseStatus: {}", ex.getReason());
        return ResponseEntity.status(ex.getStatusCode())
                .body(new ErrorResult(messageOrDefault(ex.getReason(), "error")));
    }


    private String messageOrDefault(String msg, String def) {
        return (msg == null || msg.isBlank()) ? def : msg;
    }
}
