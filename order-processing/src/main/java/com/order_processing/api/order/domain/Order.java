package com.order_processing.api.order.domain;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "DTO para registrar e gerenciar pedidos")
public record Order(
        @Schema(description = "ID do pedido", example = "1") Long id,
        @Schema(description = "Nome do cliente que realizou o pedido", example = "Angelo Lima") String customerName,
        @Schema(description = "Nome do produto solicitado no pedido", example = "Product_1") String productName,
        @Schema(description = "Quantidade do produto solicitado", example = "2") Integer quantity,
        @Schema(description = "Status atual do pedido", example = "PENDING") OrderStatus orderStatus
) {
}
