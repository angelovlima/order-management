package com.product_catalog.api.product.usecase;

import com.product_catalog.api.product.domain.Product;
import com.product_catalog.api.product.exception.DuplicateResourceException;
import com.product_catalog.api.product.gateway.ProductGateway;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class CreateProductUseCase {

    private final ProductGateway productGateway;

    public Product execute(Product product) {
        if (productGateway.existsByName(product.getName())) {
            throw new DuplicateResourceException("Produto com o nome " + product.getName() + " já existe.");
        }
        return productGateway.save(product);
    }
}
