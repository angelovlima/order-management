package com.delivery_logistics.api.delivery.gateway;

import com.delivery_logistics.api.delivery.domain.OrderStatus;

import java.time.LocalDateTime;

public interface DeliveryHistoryGateway {
    void saveHistory(Long deliveryId, OrderStatus status, LocalDateTime changedAt, String notes);
}
