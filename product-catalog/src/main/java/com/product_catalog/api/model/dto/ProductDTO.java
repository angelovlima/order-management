package com.product_catalog.api.model.dto;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "DTO para registrar e gerenciar produtos")
public record ProductDTO(
        @Schema(description = "ID do produto", example = "1") Long id,
        @Schema(description = "Nome do produto", example = "Notebook Dell Inspiron 15") String name,
        @Schema(description = "Preço do produto", example = "3500.50") Double price,
        @Schema(description = "Quantidade em estoque do produto", example = "25") Integer stockQuantity
) {
}
