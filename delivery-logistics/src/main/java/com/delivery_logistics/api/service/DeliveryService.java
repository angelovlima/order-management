package com.delivery_logistics.api.service;

import com.delivery_logistics.api.config.db.entity.DeliveryEntity;
import com.delivery_logistics.api.config.db.repository.DeliveryRepository;
import org.springframework.stereotype.Service;

@Service
public class DeliveryService {

    private final DeliveryRepository deliveryRepository;

    public DeliveryService(DeliveryRepository deliveryRepository) {
        this.deliveryRepository = deliveryRepository;
    }

    public void saveDelivery(DeliveryEntity delivery) {
        deliveryRepository.save(delivery);
    }
}
