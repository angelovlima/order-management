package com.product_catalog.api.config.batch;

import com.product_catalog.api.config.db.entity.ProductEntity;
import org.springframework.batch.item.ItemProcessor;

import java.time.LocalDateTime;

public class ProductProcessor implements ItemProcessor<ProductEntity, ProductEntity> {

    @Override
    public ProductEntity process(ProductEntity item) throws Exception {
        item.setCreateDateTime(LocalDateTime.now());
        return item;
    }
}
