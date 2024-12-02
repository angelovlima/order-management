package br.com.fiap.customer_management.api.model.dto;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "DTO para registrar e gerenciar clientes")
public record CustomerDTO(
        @Schema(description = "ID do cliente", example = "1") Long id,
        @Schema(description = "Nome do cliente", example = "Ângelo Lima") String name,
        @Schema(description = "Endereço do cliente", example = "Rua Exemplo, 123") String address,
        @Schema(description = "Contato do cliente", example = "(12) 98765-4321") String phone,
        @Schema(description = "E-mail do cliente", example = "emailexemplo@hotmail.com") String email
) {
}
