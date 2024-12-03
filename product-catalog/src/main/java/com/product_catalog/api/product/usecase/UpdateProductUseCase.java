package com.product_catalog.api.product.usecase;

import com.product_catalog.api.product.domain.Product;
import com.product_catalog.api.product.exception.DuplicateResourceException;
import com.product_catalog.api.product.exception.ResourceNotFoundException;
import com.product_catalog.api.product.gateway.ProductGateway;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UpdateProductUseCase {

    private final ProductGateway productGateway;

    public Product execute(Long id, Product product) {
        Product existingProduct = productGateway.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Produto com o id " + id + " não encontrado."));

        if (!existingProduct.getName().equals(product.getName()) && productGateway.existsByName(product.getName())) {
            throw new DuplicateResourceException("Produto com o nome " + product.getName() + " já existe.");
        }

        existingProduct.updateDetails(product.getName(), product.getPrice(), product.getStockQuantity());
        return productGateway.save(existingProduct);
    }
}
