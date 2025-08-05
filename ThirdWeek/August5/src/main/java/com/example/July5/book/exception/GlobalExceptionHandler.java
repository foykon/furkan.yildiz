package com.example.July5.book.exception;

import com.example.July5.book.dto.ErrorResponse;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.ConstraintViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class GlobalExceptionHandler {
    @ExceptionHandler(value = BookNotFoundException.class)
    public ResponseEntity<ErrorResponse> handlerBookNotFoundException(HttpServletRequest httpServletRequest, BookNotFoundException bookNotFoundException){
        ErrorResponse eRes = ErrorResponse.builder().errorCode(1000L).message(bookNotFoundException.getMessage()).build();

        return new ResponseEntity<>(eRes, HttpStatus.NOT_FOUND);
    }
    @ExceptionHandler(value = ConstraintViolationException.class)
    public ResponseEntity<ErrorResponse> handleConstraintViolationException(HttpServletRequest httpServletRequest, ConstraintViolationException constraintViolationException){
        ErrorResponse eRes = ErrorResponse.builder().errorCode(1000L).message(constraintViolationException.getMessage()).build();

        return new ResponseEntity<>(eRes, HttpStatus.BAD_REQUEST);
    }
    @ExceptionHandler(value = Exception.class)
    public ResponseEntity<ErrorResponse> handlerException(HttpServletRequest httpServletRequest, Exception exception){
        ErrorResponse eRes = ErrorResponse.builder().errorCode(1000L).message("Bilinmeyenm hata").build();

        return new ResponseEntity<>(eRes, HttpStatus.SERVICE_UNAVAILABLE);
    }


}
