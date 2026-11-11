package co.edu.cesde.ga.application.dto.response;

import java.time.LocalDateTime;

public record ErrorResponse(String message, LocalDateTime timestamp) {

    public static ErrorResponse of(String message) {
        return new ErrorResponse(message, LocalDateTime.now());
    }

    public static ErrorResponse of(String message, LocalDateTime timestamp) {
        return new ErrorResponse(message, timestamp);
    }
}