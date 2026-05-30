package com.flightdrift.flightdrift.exception;

import com.flightdrift.flightdrift.dto.ApiResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.server.ResponseStatusException;

import static org.springframework.http.HttpStatus.INTERNAL_SERVER_ERROR;

/*
 * Author: Jamius Siam
 * Since: 30/05/2026
 */
@Slf4j
@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(ResponseStatusException.class)
    public ResponseEntity<ApiResponse<?>> handleResponseStatusException(ResponseStatusException exception) {
        String message = exception.getReason() == null ? exception.getMessage() : exception.getReason();

        return ResponseEntity
                .status(exception.getStatusCode())
                .body(new ApiResponse<>(false, message, null));
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ApiResponse<?>> handleException(Exception exception) {
        log.error("Unhandled exception", exception);

        return ResponseEntity
                .status(INTERNAL_SERVER_ERROR)
                .body(new ApiResponse<>(false, "Internal server error", null));
    }
}
