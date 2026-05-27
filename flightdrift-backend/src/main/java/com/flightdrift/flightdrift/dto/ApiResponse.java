package com.flightdrift.flightdrift.dto;

/*
 * Author: Jamius Siam
 * Since: 05/05/2026
 */
public record ApiResponse<T>(
        boolean success,
        String message,
        T data
) {
}
