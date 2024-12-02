package com.order_processing.api.service;

import com.order_processing.api.model.dto.Order;

public interface OrderEventGateway {

    void sendOrderCreatedEvent(Order order);
    void sendOrderUpdateEvent(Order order);
}
