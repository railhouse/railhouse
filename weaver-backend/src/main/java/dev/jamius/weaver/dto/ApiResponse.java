package dev.jamius.weaver.dto;

public record ApiResponse<T>(
        boolean success,
        String message,
        T data
) {
}
