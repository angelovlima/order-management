package com.delivery_logistics.api.model.dto;

import io.swagger.v3.oas.annotations.media.Schema;

import java.time.LocalDateTime;

@Schema(description = "DTO para registrar e gerenciar histórico de entregas")
public record DeliveryHistoryDTO(
        @Schema(description = "ID do histórico", example = "1") Long id,
        @Schema(description = "ID da entrega associada", example = "100") Long deliveryId,
        @Schema(description = "Status do pedido", example = "DELIVERED") String status,
        @Schema(description = "Data e hora da alteração", example = "2024-12-01T10:15:30") LocalDateTime changedAt,
        @Schema(description = "Notas adicionais sobre a alteração", example = "Entregue com sucesso") String notes
) {
}
