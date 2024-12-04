package com.delivery_logistics.api.consumer;

import com.delivery_logistics.api.config.db.entity.DeliveryEntity;
import com.delivery_logistics.api.delivery.domain.OrderDelivery;
import com.delivery_logistics.api.delivery.domain.OrderStatus;
import com.delivery_logistics.api.delivery.usecase.SaveDeliveryUseCase;
import com.delivery_logistics.api.service.DeliveryService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Bean;
import org.springframework.messaging.Message;
import org.springframework.stereotype.Component;

import java.util.function.Consumer;

@Slf4j
@Component
@RequiredArgsConstructor
public class OrderConsumer {

    private final SaveDeliveryUseCase saveDeliveryUseCase;

    @Bean
    public Consumer<Message<OrderDelivery>> processOrder() {
        return message -> {
            OrderDelivery orderDelivery = message.getPayload();
            log.info("Order received: {}", orderDelivery);

            try {
                DeliveryEntity delivery = new DeliveryEntity(
                        orderDelivery.idCustomer(),
                        orderDelivery.idProduct(),
                        orderDelivery.quantity(),
                        orderDelivery.paymentMethod(),
                        OrderStatus.PENDING
                );

                saveDeliveryUseCase.execute(delivery);
                log.info("DeliveryEntity saved successfully: {}", delivery);

            } catch (Exception ex) {
                log.error("Error processing order: {}", orderDelivery, ex);
            }
        };
    }
}