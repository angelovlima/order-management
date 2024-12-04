package com.delivery_logistics.api.delivery.usecase;

import com.delivery_logistics.api.config.db.entity.DeliveryEntity;
import com.delivery_logistics.api.delivery.domain.Delivery;
import com.delivery_logistics.api.delivery.gateway.DeliveryGateway;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class SaveDeliveryUseCase {

    private final DeliveryGateway deliveryGateway;

    public void execute(DeliveryEntity delivery) {
        deliveryGateway.save(delivery);
    }
}
