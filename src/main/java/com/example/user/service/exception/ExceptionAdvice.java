package com.example.user.service.exception;

import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.time.Instant;
import java.util.List;

@Slf4j
@RestControllerAdvice
public class ExceptionAdvice {

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ExceptionResponseDto handle(MethodArgumentNotValidException exception, HttpServletRequest request) {
        List<String> invalidFields = exception.getBindingResult().getFieldErrors().stream()
                .map(FieldError::getField)
                .toList();
        String errorMessage = ErrorMessages.INVALID_DTO_PARAMETERS.formatted(
                String.join(", ", invalidFields)
        );
        log.error(errorMessage);
        return ExceptionResponseDto.builder()
                .status(HttpStatus.BAD_REQUEST.value())
                .timestamp(Instant.now())
                .message(errorMessage)
                .path(request.getRequestURI())
                .build();
    }

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(HttpMessageNotReadableException.class)
    public ExceptionResponseDto handle(HttpServletRequest request) {
        log.error(ErrorMessages.MISSING_REQUEST_BODY);
        return ExceptionResponseDto.builder()
                .status(HttpStatus.BAD_REQUEST.value())
                .timestamp(Instant.now())
                .message(ErrorMessages.MISSING_REQUEST_BODY)
                .path(request.getRequestURI())
                .build();
    }

}
