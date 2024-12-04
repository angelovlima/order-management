package com.delivery_logistics.api.delivery.gateway;

import com.delivery_logistics.api.config.db.entity.DeliveryEntity;
import com.delivery_logistics.api.delivery.domain.OrderStatus;

import java.util.Optional;

public interface DeliveryGateway {
    Optional<OrderStatus> findStatusById(Long deliveryId);
    void save(DeliveryEntity delivery);
}
