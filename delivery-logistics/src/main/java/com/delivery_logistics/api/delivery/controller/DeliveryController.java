package com.delivery_logistics.api.delivery.controller;

import com.delivery_logistics.api.delivery.domain.OrderStatus;
import com.delivery_logistics.api.delivery.usecase.CheckDeliveryStatusUseCase;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
public class DeliveryController {

    private final CheckDeliveryStatusUseCase checkDeliveryStatusUseCase;

    @GetMapping("/delivery/status")
    public ResponseEntity<OrderStatus> getDeliveryStatus(@RequestParam Long deliveryId) {
        OrderStatus status = checkDeliveryStatusUseCase.execute(deliveryId);
        return ResponseEntity.ok(status);
    }
}
