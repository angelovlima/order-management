package com.delivery_logistics.api.model.dto;

import com.delivery_logistics.api.model.enums.PaymentMethod;
import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "DTO para representar pedidos")
public record OrderDeliveryDTO(
        @Schema(description = "ID do cliente", example = "123") Long idCustomer,
        @Schema(description = "ID do produto", example = "456") Long idProduct,
        @Schema(description = "Quantidade do produto", example = "10") Integer quantity,
        @Schema(description = "Método de pagamento", example = "CREDIT_CARD") PaymentMethod paymentMethod,
        @Schema(description = "Status do pedido", example = "PENDING") String orderStatus
) {
}