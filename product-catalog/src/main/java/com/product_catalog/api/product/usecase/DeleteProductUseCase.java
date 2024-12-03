package com.product_catalog.api.product.usecase;

import com.product_catalog.api.product.exception.ResourceNotFoundException;
import com.product_catalog.api.product.gateway.ProductGateway;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class DeleteProductUseCase {

    private final ProductGateway productGateway;

    public void execute(Long id) {
        if (!productGateway.existsById(id)) {
            throw new ResourceNotFoundException("Produto com o id " + id + " não encontrado.");
        }
        productGateway.deleteById(id);
    }
}
