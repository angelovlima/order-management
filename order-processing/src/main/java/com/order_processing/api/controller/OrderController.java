package com.order_processing.api.controller;

import com.order_processing.api.client.CustomerClient;
import com.order_processing.api.client.ProductClient;
import com.order_processing.api.exception.CustomerNotFoundException;
import com.order_processing.api.model.dto.Order;
import com.order_processing.api.model.dto.OrderDeliveryDTO;
import com.order_processing.api.model.enums.OrderStatus;
import com.order_processing.api.model.enums.PaymentMethod;
import com.order_processing.api.producer.OrderProducer;
import com.order_processing.api.service.OrderEventGateway;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/order")
public class OrderController {

    private final OrderProducer orderProducer;
//    private final OrderEventGateway orderEventGateway;
    private final CustomerClient customerClient;
    private final ProductClient productClient;

    @Autowired
    public OrderController(
            OrderProducer orderProducer,
            OrderEventGateway orderEventGateway,
            CustomerClient customerClient,
            ProductClient productClient) {
        this.orderProducer = orderProducer;
//        this.orderEventGateway = orderEventGateway;
        this.customerClient = customerClient;
        this.productClient = productClient;

    }

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
    public ResponseEntity<String> createOrder(@RequestBody @Valid OrderDeliveryDTO orderDeliveryDTO) {
        // Passo 1: Validar Cliente
        customerClient.validateCustomer(orderDeliveryDTO.idCustomer());
//        if (!isCustomerValid) {
//            throw new CustomerNotFoundException("Cliente com ID " + orderDeliveryDTO.idCustomer() + " não encontrado.");
//        }

        // Passo 2: Validar Produto e Estoque
        productClient.validateProduct(orderDeliveryDTO.idProduct(), orderDeliveryDTO.quantity());

        // Passo 3: Enviar mensagem para o RabbitMQ
        orderProducer.sendOrder(orderDeliveryDTO);

        return ResponseEntity.ok("Pedido enviado com sucesso para entrega.");


//        orderEventGateway.sendOrderCreatedEvent(order);

        //Talvez daria para fazer a busca do usuário por aqui, se sobrar tempo

        //O sistema faz a busca http no serviço catalogo para verificar se ainda existe estoque
        //FAZER AQUI

        //Caso tenha estoque ele vem aqui em abre um pedido


//        OrderDeliveryDTO mock = new OrderDeliveryDTO(
//                123L,             // idCustomer
//                456L,             // idProduct
//                10,               // quantity
//                PaymentMethod.CREDIT_CARD, // paymentMethod
//                "PENDING"         // orderStatus
//        );
//        orderProducer.sendOrder(mock);

        //O estoque vai consumir a mensagem e descontar stock_quantity, não haverá retorno

        //Mandar mensagem para o serviço de entrega para enviar o pedido
//        return ResponseEntity.ok("Pedido enviado com sucesso com o status: " + orderDeliveryDTO.orderStatus());
    }
}
