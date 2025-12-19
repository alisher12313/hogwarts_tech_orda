package com.pm.hogwarts.exceptionHandler;

import com.pm.hogwarts.dto.ApiError;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.time.Instant;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(ExternalApiE.class)
    public ResponseEntity<?> handleExternalApi(ExternalApiE ex, HttpServletRequest req) {
        ApiError body = new ApiError(
                Instant.now(),
                HttpStatus.SERVICE_UNAVAILABLE.value(),
                "External API error",
                ex.getMessage(),
                req.getRequestURI()
        );
        return ResponseEntity.status(HttpStatus.SERVICE_UNAVAILABLE).body(body);
    }

    @ExceptionHandler(BadRequestE.class)
    public ResponseEntity<ApiError> handleBadRequest(BadRequestE ex, HttpServletRequest req) {
        ApiError body = new ApiError(
                Instant.now(),
                HttpStatus.BAD_REQUEST.value(),
                "Bad request",
                ex.getMessage(),
                req.getRequestURI()
        );
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(body);
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ApiError> handleAny(Exception ex, HttpServletRequest req) {
        ApiError body = new ApiError(
                Instant.now(),
                HttpStatus.INTERNAL_SERVER_ERROR.value(),
                "Internal server error",
                "Something went wrong.",
                req.getRequestURI()
        );
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(body);
    }
}
