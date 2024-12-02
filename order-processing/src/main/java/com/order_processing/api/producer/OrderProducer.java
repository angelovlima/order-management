package com.order_processing.api.producer;

import com.order_processing.api.model.dto.OrderDeliveryDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.stream.function.StreamBridge;
import org.springframework.stereotype.Component;

@Component
public class OrderProducer {

    private final StreamBridge streamBridge;

    @Autowired
    public OrderProducer(StreamBridge streamBridge) {
        this.streamBridge = streamBridge;
    }

    public void sendOrder(OrderDeliveryDTO orderDeliveryDTO) {
        boolean sent = streamBridge.send("processOrder-out-0", orderDeliveryDTO);
        if (!sent) {
            throw new RuntimeException("Falha ao enviar a mensagem para o RabbitMQ");
        }
    }
}