package com.delivery_logistics.api.model.dto;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "DTO para registrar e gerenciar entregas")
public record DeliveryDTO(
        @Schema(description = "ID da entrega", example = "1") Long id,
        @Schema(description = "ID do cliente", example = "2") Long idCustomer,
        @Schema(description = "ID do produto", example = "220") Long idProduct,
        @Schema(description = "Quantidade do produto", example = "3") Integer quantity,
        @Schema(description = "Método de pagamento", example = "CREDIT_CARD") String paymentMethod,
        @Schema(description = "Status do pedido", example = "PENDING") String orderStatus
) {
}
