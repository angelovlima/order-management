package com.order_processing.api.order.gateway.impl;

import com.order_processing.api.order.domain.OrderDelivery;
import com.order_processing.api.order.domain.PaymentMethod;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.cloud.stream.binder.test.EnableTestBinder;
import org.springframework.cloud.stream.binder.test.OutputDestination;
import org.springframework.messaging.Message;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

@SpringBootTest
@EnableTestBinder
class OrderProducerTest {

    @Autowired
    private OrderProducer orderProducer;

    @Autowired
    private OutputDestination outputDestination;

    @Test
    void shouldSendOrderSuccessfully() {
        OrderDelivery orderDelivery = new OrderDelivery(
                123L,
                456L,
                10,
                PaymentMethod.CREDIT_CARD,
                "PENDING"
        );

        orderProducer.sendOrder(orderDelivery);

        Message<byte[]> sentMessage = outputDestination.receive(0, "orders.destination");
        assertThat(sentMessage).isNotNull();
        String payload = new String(sentMessage.getPayload());
        assertThat(payload).contains("\"idCustomer\":123");
        assertThat(payload).contains("\"idProduct\":456");
        assertThat(payload).contains("\"quantity\":10");
        assertThat(payload).contains("\"paymentMethod\":\"CREDIT_CARD\"");
        assertThat(payload).contains("\"orderStatus\":\"PENDING\"");
    }

}
