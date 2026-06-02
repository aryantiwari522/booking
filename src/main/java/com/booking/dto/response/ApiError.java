package com.booking.dto.response;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.Map;

@Data
@AllArgsConstructor
public class ApiError {
    private String message;
    private Map<String, String> errors;

    public ApiError(String message) {
        this(message, Map.of());
    }
}
