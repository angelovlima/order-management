package com.order_processing.api.order.gateway.impl;

import com.order_processing.api.order.domain.OrderDelivery;
import com.order_processing.api.order.gateway.OrderGateway;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.stream.function.StreamBridge;
import org.springframework.stereotype.Component;

@Component
public class OrderProducer implements OrderGateway {

    private final StreamBridge streamBridge;

    @Autowired
    public OrderProducer(StreamBridge streamBridge) {
        this.streamBridge = streamBridge;
    }

    public void sendOrder(OrderDelivery orderDelivery) {
        boolean sent = streamBridge.send("processOrder-out-0", orderDelivery);
        if (!sent) {
            throw new RuntimeException("Falha ao enviar a mensagem para o RabbitMQ");
        }
    }
}