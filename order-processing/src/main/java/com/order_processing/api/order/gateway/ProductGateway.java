package com.order_processing.api.order.gateway;

public interface ProductGateway {
    void validateProduct(Long productId, Integer requiredQuantity);
}