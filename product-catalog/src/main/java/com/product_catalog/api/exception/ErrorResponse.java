package com.product_catalog.api.exception;

import java.time.LocalDateTime;
import java.util.List;

public record ErrorResponse(
        String message,
        List<String> details,
        LocalDateTime timestamp
) {
}
