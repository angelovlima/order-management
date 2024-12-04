package com.delivery_logistics.api.delivery.usecase;

import com.delivery_logistics.api.delivery.domain.OrderStatus;
import com.delivery_logistics.api.delivery.gateway.DeliveryGateway;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class CheckDeliveryStatusUseCase {

    private final DeliveryGateway deliveryGateway;

    public OrderStatus execute(Long deliveryId) {
        return deliveryGateway.findStatusById(deliveryId)
                .orElseThrow(() -> new IllegalArgumentException("Delivery not found for id: " + deliveryId));
    }
}
