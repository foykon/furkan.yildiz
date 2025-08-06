package com.furkan.project.auth.exception;

import com.furkan.project.auth.entity.Role;
import lombok.Getter;
import lombok.Setter;
import org.springframework.data.crossstore.ChangeSetPersister;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

@ControllerAdvice
public class GlobalExceptionHandler {

    // runtime
    @ExceptionHandler(RuntimeException.class)
    public ResponseEntity<?> handleRuntimeException(RuntimeException ex) {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                .body(new ErrorResponse(HttpStatus.BAD_REQUEST.value(), LocalDateTime.now(),"Bad Request", ex.getMessage()));
    }

    // DTO Validation @valid
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<?> handleException(Exception ex) {
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(new ErrorResponse(HttpStatus.INTERNAL_SERVER_ERROR.value(), LocalDateTime.now(), "Internal Server Error", "Beklenmedik bir hata oluştu"));
    }

    @ExceptionHandler(UserAlreadyExistsException.class)
    public ResponseEntity<ErrorResponse> handleUserAlreadyExists(UserAlreadyExistsException ex) {
        return ResponseEntity
                .status(HttpStatus.CONFLICT)
                .body(new ErrorResponse( HttpStatus.CONFLICT.value(), LocalDateTime.now(), "USer already exists", ex.getMessage()));
    }
    @ExceptionHandler(RoleNotFoundException.class)
        public ResponseEntity<ErrorResponse> handleRoleNotFound(RoleNotFoundException ex) {
            return ResponseEntity
                    .status(HttpStatus.CONFLICT)
                    .body(new ErrorResponse( HttpStatus.CONFLICT.value(), LocalDateTime.now(), "Role not found", ex.getMessage()));
        }
    @ExceptionHandler(UserNotFoundException.class)
            public ResponseEntity<ErrorResponse> handleUserNotFound(UserNotFoundException ex) {
                return ResponseEntity
                        .status(HttpStatus.CONFLICT)
                        .body(new ErrorResponse( HttpStatus.CONFLICT.value(), LocalDateTime.now(), "User not found", ex.getMessage()));
            }

    @ExceptionHandler(InvalidCredentialsException.class)
    public ResponseEntity<ErrorResponse> handleInvalidCredentials(InvalidCredentialsException ex) {
        return ResponseEntity
                .status(HttpStatus.UNAUTHORIZED)
                .body(new ErrorResponse( HttpStatus.UNAUTHORIZED.value(),LocalDateTime.now(),"Invalid Credentials", ex.getMessage()));
    }

}
