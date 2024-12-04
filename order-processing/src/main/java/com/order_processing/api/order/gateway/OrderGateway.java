package com.order_processing.api.order.gateway;

import com.order_processing.api.order.domain.OrderDelivery;

public interface OrderGateway {
    void sendOrder(OrderDelivery orderDelivery);
}
