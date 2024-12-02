package com.product_catalog.api.config;

import com.product_catalog.api.model.entity.Product;
import org.springframework.batch.item.ItemProcessor;

import java.time.LocalDateTime;

public class ProductProcessor implements ItemProcessor<Product, Product> {

    @Override
    public Product process(Product item) throws Exception {
        item.setCreateDateTime(LocalDateTime.now());
        return item;
    }
}
