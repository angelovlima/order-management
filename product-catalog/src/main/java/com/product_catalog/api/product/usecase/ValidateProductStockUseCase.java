package com.product_catalog.api.product.usecase;

import com.product_catalog.api.product.domain.Product;
import com.product_catalog.api.product.exception.ResourceNotFoundException;
import com.product_catalog.api.product.gateway.ProductGateway;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ValidateProductStockUseCase {

    private final ProductGateway productGateway;

    public void execute(Long productId, Integer requestedQuantity) {
        Product product = productGateway.findById(productId)
                .orElseThrow(() -> new ResourceNotFoundException("Produto com o id " + productId + " não encontrado."));

        if (requestedQuantity > product.getStockQuantity()) {
            throw new IllegalArgumentException("Estoque insuficiente para o produto com o id " + productId);
        }

        productGateway.updateStock(productId, product.getStockQuantity() - requestedQuantity);
    }
}
