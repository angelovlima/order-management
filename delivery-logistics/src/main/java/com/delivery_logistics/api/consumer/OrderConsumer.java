package com.delivery_logistics.api.consumer;

import com.delivery_logistics.api.model.dto.OrderDeliveryDTO;
import com.delivery_logistics.api.model.entity.Delivery;
import com.delivery_logistics.api.model.enums.OrderStatus;
import com.delivery_logistics.api.service.DeliveryService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Bean;
import org.springframework.messaging.Message;
import org.springframework.stereotype.Component;

import java.util.function.Consumer;

@Slf4j
@Component
public class OrderConsumer {

    private final DeliveryService deliveryService;

    public OrderConsumer(DeliveryService deliveryService) {
        this.deliveryService = deliveryService;
    }

    @Bean
    public Consumer<Message<OrderDeliveryDTO>> processOrder() {
        return message -> {
            OrderDeliveryDTO orderDeliveryDTO = message.getPayload();
            log.info("Order received: {}", orderDeliveryDTO);

            try {
                // Processar a mensagem e salvar nos dados de entrega
                Delivery delivery = new Delivery(
                        orderDeliveryDTO.idCustomer(),
                        orderDeliveryDTO.idProduct(),
                        orderDeliveryDTO.quantity(),
                        orderDeliveryDTO.paymentMethod(),
                        OrderStatus.PENDING // Define como pendente inicialmente
                );

                deliveryService.saveDelivery(delivery);
                log.info("Delivery saved successfully: {}", delivery);

            } catch (Exception ex) {
                log.error("Error processing order: {}", orderDeliveryDTO, ex);
                // Tratar a falha adequadamente, como publicar em uma DLQ
            }
        };
    }
}