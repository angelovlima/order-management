package com.order_processing.api.model.dto;

public record ProductDTO(
    Long id,
    String name,
    Double price,
    Integer stockQuantity
) {
}
