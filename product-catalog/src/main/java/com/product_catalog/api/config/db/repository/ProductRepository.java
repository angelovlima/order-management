package com.product_catalog.api.config.db.repository;

import com.product_catalog.api.config.db.entity.ProductEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ProductRepository extends JpaRepository<ProductEntity, Long> {
    boolean existsByName(String name);
}
