package com.order_processing.api.model.dto;

public record CustomerDTO(
        Long id,
        String name,
        String address,
        String phone,
        String email
) {
}
