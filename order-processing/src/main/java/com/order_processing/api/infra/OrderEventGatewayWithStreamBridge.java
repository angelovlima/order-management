package com.order_processing.api.infra;

import com.order_processing.api.config.OrderProperties;
import com.order_processing.api.model.dto.Order;
import com.order_processing.api.service.OrderEventGateway;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cloud.stream.function.StreamBridge;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
@Slf4j
public class OrderEventGatewayWithStreamBridge implements OrderEventGateway {

    private final StreamBridge streamBridge;
    private final OrderProperties orderProperties;
//    private final Mapper mapper;

    @Override
    public void sendOrderCreatedEvent(Order order) {
        log.info("Sending order created event: {}", order.id());
        streamBridge.send(orderProperties.getOrderCreatedChannel(), order);
    }

    @Override
    public void sendOrderUpdateEvent(Order order) {
        log.info("Sending order update event: {}", order.id());
        streamBridge.send(orderProperties.getOrderUpdatedChannel(), order);
    }
}
