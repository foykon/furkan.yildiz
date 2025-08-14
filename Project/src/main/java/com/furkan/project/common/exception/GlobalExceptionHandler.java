package com.furkan.project.common.exception;

import com.furkan.project.common.result.ErrorDataResult;
import com.furkan.project.common.result.ErrorResult;
import com.furkan.project.common.result.Result;
import jakarta.persistence.EntityNotFoundException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.ConstraintViolationException;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.MDC;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.validation.FieldError;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;
import org.springframework.web.server.ResponseStatusException;

import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Optional;

@Slf4j
@RestControllerAdvice
public class GlobalExceptionHandler {

    // -------- 400s

    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<Result> handleIllegalArgument(IllegalArgumentException ex, HttpServletRequest req) {
        var status = HttpStatus.BAD_REQUEST;
        String code = messageOrDefault(ex.getMessage(), "bad.request");
        logClientError(req, ex, status, code);
        return ResponseEntity.status(status).body(new ErrorResult(code));
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ErrorDataResult<Map<String, String>>> handleBeanValidation(MethodArgumentNotValidException ex,
                                                                                     HttpServletRequest req) {
        Map<String, String> errors = new LinkedHashMap<>();
        for (FieldError fe : ex.getBindingResult().getFieldErrors()) {
            errors.put(fe.getField(), fe.getDefaultMessage());
        }
        logClientError(req, ex, HttpStatus.BAD_REQUEST, "validation.failed");
        return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                .body(new ErrorDataResult<>(errors, "validation.failed"));
    }

    @ExceptionHandler(ConstraintViolationException.class)
    public ResponseEntity<ErrorDataResult<Map<String, String>>> handleConstraint(ConstraintViolationException ex,
                                                                                 HttpServletRequest req) {
        Map<String, String> errors = new LinkedHashMap<>();
        ex.getConstraintViolations().forEach(v -> errors.put(v.getPropertyPath().toString(), v.getMessage()));
        logClientError(req, ex, HttpStatus.BAD_REQUEST, "validation.failed");
        return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                .body(new ErrorDataResult<>(errors, "validation.failed"));
    }

    @ExceptionHandler(MissingServletRequestParameterException.class)
    public ResponseEntity<Result> handleMissingParam(MissingServletRequestParameterException ex, HttpServletRequest req) {
        String code = "missing.parameter." + ex.getParameterName();
        logClientError(req, ex, HttpStatus.BAD_REQUEST, code);
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new ErrorResult(code));
    }

    @ExceptionHandler(HttpMessageNotReadableException.class)
    public ResponseEntity<Result> handleNotReadable(HttpMessageNotReadableException ex, HttpServletRequest req) {
        logClientError(req, ex, HttpStatus.BAD_REQUEST, "malformed.json");
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new ErrorResult("malformed.json"));
    }

    @ExceptionHandler(MethodArgumentTypeMismatchException.class)
    public ResponseEntity<Result> handleTypeMismatch(MethodArgumentTypeMismatchException ex, HttpServletRequest req) {
        String code = "parameter.type.mismatch." + ex.getName();
        logClientError(req, ex, HttpStatus.BAD_REQUEST, code);
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new ErrorResult(code));
    }

    // -------- 401/403

    @ExceptionHandler(BadCredentialsException.class)
    public ResponseEntity<Result> handleBadCredentials(BadCredentialsException ex, HttpServletRequest req) {
        logClientError(req, ex, HttpStatus.UNAUTHORIZED, "auth.invalid.credentials");
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(new ErrorResult("auth.invalid.credentials"));
    }

    @ExceptionHandler(AccessDeniedException.class)
    public ResponseEntity<Result> handleAccessDenied(AccessDeniedException ex, HttpServletRequest req) {
        logClientError(req, ex, HttpStatus.FORBIDDEN, "auth.forbidden");
        return ResponseEntity.status(HttpStatus.FORBIDDEN).body(new ErrorResult("auth.forbidden"));
    }

    // -------- 404

    @ExceptionHandler(EntityNotFoundException.class)
    public ResponseEntity<Result> handleEntityNotFound(EntityNotFoundException ex, HttpServletRequest req) {
        String code = messageOrDefault(ex.getMessage(), "resource.notfound");
        logClientError(req, ex, HttpStatus.NOT_FOUND, code);
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new ErrorResult(code));
    }

    // -------- 405

    @ExceptionHandler(HttpRequestMethodNotSupportedException.class)
    public ResponseEntity<Result> handleMethodNotAllowed(HttpRequestMethodNotSupportedException ex, HttpServletRequest req) {
        logClientError(req, ex, HttpStatus.METHOD_NOT_ALLOWED, "method.not.allowed");
        return ResponseEntity.status(HttpStatus.METHOD_NOT_ALLOWED).body(new ErrorResult("method.not.allowed"));
    }

    // -------- 409

    @ExceptionHandler(DataIntegrityViolationException.class)
    public ResponseEntity<Result> handleDataIntegrity(DataIntegrityViolationException ex, HttpServletRequest req) {
        String code = resolveConflictCode(ex);
        logClientError(req, ex, HttpStatus.CONFLICT, code);
        return ResponseEntity.status(HttpStatus.CONFLICT)
                .header("X-Error-Code", code)
                .body(new ErrorResult(code));
    }

    // -------- ResponseStatusException (çeşitli)

    @ExceptionHandler(ResponseStatusException.class)
    public ResponseEntity<Result> handleResponseStatus(ResponseStatusException ex, HttpServletRequest req) {
        String code = messageOrDefault(ex.getReason(), "error");
        HttpStatus status = HttpStatus.resolve(ex.getStatusCode().value());
        if (status == null) status = HttpStatus.INTERNAL_SERVER_ERROR;

        if (status.is4xxClientError()) {
            logClientError(req, ex, status, code);
        } else {
            logServerError(req, ex, status, code);
        }
        return ResponseEntity.status(status).body(new ErrorResult(code));
    }

    // -------- 500 (catch-all)



    // ===== Helpers =====

    private void logClientError(HttpServletRequest req, Exception ex, HttpStatus status, String code) {
        String path = req.getRequestURI() + (req.getQueryString() != null ? "?" + req.getQueryString() : "");
        String corr = nvl(MDC.get("correlationId"), "");
        String user = nvl(MDC.get("userId"), "");
        String cause = rootMessage(ex);
        // 4xx: WARN, stack trace YOK
        log.warn("http error status={} method={} path={} code={} error={} message={} corr={} user={}",
                status.value(), req.getMethod(), path, nvl(code, "-"),
                ex.getClass().getSimpleName(), cause, corr, user);
    }

    private void logServerError(HttpServletRequest req, Exception ex, HttpStatus status, String code) {
        String path = req.getRequestURI() + (req.getQueryString() != null ? "?" + req.getQueryString() : "");
        String corr = nvl(MDC.get("correlationId"), "");
        String user = nvl(MDC.get("userId"), "");
        String cause = rootMessage(ex);
        // 5xx: ERROR, stack trace VAR
        log.error("http error status={} method={} path={} code={} error={} message={} corr={} user={}",
                status.value(), req.getMethod(), path, nvl(code, "-"),
                ex.getClass().getSimpleName(), cause, corr, user, ex);
    }

    private String resolveConflictCode(DataIntegrityViolationException ex) {
        Throwable cause = ex.getCause();
        if (cause instanceof org.hibernate.exception.ConstraintViolationException h) {
            String c = Optional.ofNullable(h.getConstraintName()).orElse("").toLowerCase();
            if (c.contains("uk_user_email") || c.contains("uq_user_email")) return "email.taken";
            if (c.contains("uk_user_username") || c.contains("uq_user_username")) return "username.taken";
            if (c.contains("uq_user_list_movie") || c.contains("uk_user_movie")) return "movie.already.in.list";
        }
        String msg = Optional.ofNullable(ex.getMostSpecificCause()).map(Throwable::getMessage).orElse("").toLowerCase();
        if (msg.contains("email")) return "email.taken";
        if (msg.contains("username")) return "username.taken";
        return "data.integrity.violation";
    }

    private String rootMessage(Throwable t) {
        Throwable r = t;
        while (r.getCause() != null && r.getCause() != r) r = r.getCause();
        return (r.getMessage() != null ? r.getMessage() : r.toString());
    }

    private String messageOrDefault(String msg, String def) {
        return (msg == null || msg.isBlank()) ? def : msg;
    }

    private String nvl(String s, String d) { return (s == null || s.isBlank()) ? d : s; }
}
