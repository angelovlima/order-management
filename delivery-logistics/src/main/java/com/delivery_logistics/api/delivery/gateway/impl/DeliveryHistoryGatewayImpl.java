package com.delivery_logistics.api.delivery.gateway.impl;

import com.delivery_logistics.api.config.db.entity.DeliveryHistoryEntity;
import com.delivery_logistics.api.config.db.repository.DeliveryHistoryRepository;
import com.delivery_logistics.api.config.db.repository.DeliveryRepository;
import com.delivery_logistics.api.delivery.domain.OrderStatus;
import com.delivery_logistics.api.delivery.gateway.DeliveryHistoryGateway;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;

@Component
@RequiredArgsConstructor
public class DeliveryHistoryGatewayImpl implements DeliveryHistoryGateway {

    private final DeliveryHistoryRepository deliveryHistoryRepository;
    private final DeliveryRepository deliveryRepository;

    @Override
    public void saveHistory(Long deliveryId, OrderStatus status, LocalDateTime changedAt, String notes) {
        var delivery = deliveryRepository.findById(deliveryId)
                .orElseThrow(() -> new IllegalArgumentException("Delivery not found for id: " + deliveryId));

        DeliveryHistoryEntity history = new DeliveryHistoryEntity(delivery, status, changedAt, notes);
        deliveryHistoryRepository.save(history);
    }
}