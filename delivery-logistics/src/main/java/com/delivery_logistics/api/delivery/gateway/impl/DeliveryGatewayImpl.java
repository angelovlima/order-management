package com.delivery_logistics.api.delivery.gateway.impl;

import com.delivery_logistics.api.config.db.entity.DeliveryEntity;
import com.delivery_logistics.api.config.db.repository.DeliveryRepository;
import com.delivery_logistics.api.delivery.domain.OrderStatus;
import com.delivery_logistics.api.delivery.gateway.DeliveryGateway;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component
@RequiredArgsConstructor
public class DeliveryGatewayImpl implements DeliveryGateway {

    private final DeliveryRepository deliveryRepository;

    @Override
    public Optional<OrderStatus> findStatusById(Long deliveryId) {
        return deliveryRepository.findById(deliveryId)
                .map(DeliveryEntity::getOrderStatus);
    }

    @Override
    public void save(DeliveryEntity delivery) {
        deliveryRepository.save(delivery);
    }
}
