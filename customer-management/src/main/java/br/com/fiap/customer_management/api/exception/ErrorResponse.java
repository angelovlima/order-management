package br.com.fiap.customer_management.api.exception;

import java.time.LocalDateTime;
import java.util.List;

public record ErrorResponse(
        String message,
        List<String> details,
        LocalDateTime timestamp
) {
}
