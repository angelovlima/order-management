package com.product_catalog.api.product.gateway.impl;

import com.product_catalog.api.config.db.entity.ProductEntity;
import com.product_catalog.api.config.db.repository.ProductRepository;
import com.product_catalog.api.product.domain.Product;
import com.product_catalog.api.product.exception.ResourceNotFoundException;
import com.product_catalog.api.product.gateway.ProductGateway;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Component
@RequiredArgsConstructor
public class ProductGatewayImpl implements ProductGateway {

    private final ProductRepository productRepository;

    @Override
    public Product save(Product product) {
        ProductEntity entity = toEntity(product);
        ProductEntity savedEntity = productRepository.save(entity);
        return toDomain(savedEntity);
    }

    @Override
    public Optional<Product> findById(Long id) {
        return productRepository.findById(id).map(this::toDomain);
    }

    @Override
    public List<Product> findAll() {
        return productRepository.findAll()
                .stream()
                .map(this::toDomain)
                .collect(Collectors.toList());
    }

    @Override
    public boolean existsByName(String name) {
        return productRepository.existsByName(name);
    }

    @Override
    public boolean existsById(Long id) {
        return productRepository.existsById(id);
    }

    @Override
    public void deleteById(Long id) {
        productRepository.deleteById(id);
    }

    @Override
    public void updateStock(Long productId, Integer newStockQuantity) {
        ProductEntity product = productRepository.findById(productId)
                .orElseThrow(() -> new ResourceNotFoundException("Produto com o id " + productId + " não encontrado."));
        product.setStockQuantity(newStockQuantity);
        productRepository.save(product);
    }

    private Product toDomain(ProductEntity entity) {
        return new Product(
                entity.getId(),
                entity.getName(),
                entity.getPrice(),
                entity.getStockQuantity()
        );
    }

    private ProductEntity toEntity(Product product) {
        return new ProductEntity(
                product.getId(),
                product.getName(),
                product.getPrice(),
                product.getStockQuantity(),
                LocalDateTime.now()
        );
    }
}
