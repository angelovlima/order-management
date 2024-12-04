package com.order_processing.api.order.controller;

import com.order_processing.api.order.domain.Order;
import com.order_processing.api.order.domain.OrderDelivery;
import com.order_processing.api.order.usecase.CreateOrderUseCase;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RequiredArgsConstructor
@RestController
@RequestMapping("/order")
public class OrderController {

    private final CreateOrderUseCase createOrderUseCase;

    @PostMapping
    @Operation(
            summary = "Criar Pedido",
            description = "Cria um novo pedido e o envia para processamento via RabbitMQ.",
            requestBody = @io.swagger.v3.oas.annotations.parameters.RequestBody(
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = Order.class),
                            examples = {
                                    @ExampleObject(
                                            name = "Exemplo de Pedido",
                                            summary = "Exemplo de dados para criar um pedido",
                                            value = "{ \"idCustomer\": 123, \"idProduct\": 456, \"quantity\": 10, \"paymentMethod\": \"CREDIT_CARD\", \"orderStatus\": \"PENDING\" }"
                                    )
                            }
                    )
            )
    )
    public ResponseEntity<String> createOrder(@RequestBody @Valid OrderDelivery orderDeliveryDTO) {
        createOrderUseCase.execute(orderDeliveryDTO);
        return ResponseEntity.ok("Pedido enviado com sucesso para entrega.");
    }
}
