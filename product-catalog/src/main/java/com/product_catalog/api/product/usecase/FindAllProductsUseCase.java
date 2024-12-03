package com.product_catalog.api.product.usecase;

import com.product_catalog.api.product.domain.Product;
import com.product_catalog.api.product.gateway.ProductGateway;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class FindAllProductsUseCase {

    private final ProductGateway productGateway;

    public List<Product> execute() {
        return productGateway.findAll();
    }
}
