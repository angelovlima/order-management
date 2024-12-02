package com.delivery_logistics.api.service;

import com.delivery_logistics.api.model.entity.Delivery;
import com.delivery_logistics.api.model.repository.DeliveryRepository;
import org.springframework.stereotype.Service;

@Service
public class DeliveryService {

    private final DeliveryRepository deliveryRepository;

    public DeliveryService(DeliveryRepository deliveryRepository) {
        this.deliveryRepository = deliveryRepository;
    }

    public void saveDelivery(Delivery delivery) {
        deliveryRepository.save(delivery);
    }
}
