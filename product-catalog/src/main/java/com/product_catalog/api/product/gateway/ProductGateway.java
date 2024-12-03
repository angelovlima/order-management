package com.product_catalog.api.product.gateway;

import com.product_catalog.api.product.domain.Product;

import java.util.List;
import java.util.Optional;

public interface ProductGateway {
    Product save(Product product);
    Optional<Product> findById(Long id);
    List<Product> findAll();
    boolean existsByName(String name);
    boolean existsById(Long id);
    void deleteById(Long id);
    void updateStock(Long productId, Integer newStockQuantity);
}
