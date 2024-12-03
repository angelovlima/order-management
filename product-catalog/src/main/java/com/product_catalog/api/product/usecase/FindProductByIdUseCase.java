package com.product_catalog.api.product.usecase;

import com.product_catalog.api.product.domain.Product;
import com.product_catalog.api.product.exception.ResourceNotFoundException;
import com.product_catalog.api.product.gateway.ProductGateway;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class FindProductByIdUseCase {

    private final ProductGateway productGateway;

    public Product execute(Long id) {
        return productGateway.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Produto com o id " + id + " não encontrado."));
    }
}
