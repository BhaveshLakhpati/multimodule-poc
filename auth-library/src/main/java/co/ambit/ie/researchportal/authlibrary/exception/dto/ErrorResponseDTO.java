package co.ambit.ie.researchportal.authlibrary.exception.dto;

import lombok.Builder;

import java.time.LocalDateTime;

@Builder
public record ErrorResponseDTO(int status,
                               String error,
                               String message,
                               Object data,
                               String path,
                               String traceId,
                               LocalDateTime timestamp) {
    public ErrorResponseDTO {
        timestamp = LocalDateTime.now();
    }
}
