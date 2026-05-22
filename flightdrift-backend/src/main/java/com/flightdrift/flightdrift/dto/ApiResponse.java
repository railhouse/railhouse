package com.flightdrift.flightdrift.dto;

public record ApiResponse<T>(
        boolean success,
        String message,
        T data
) {
}
