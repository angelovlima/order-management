package com.product_catalog.api.product.domain;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public class Product {
    private final Long id;
    private String name;
    private Double price;
    private Integer stockQuantity;

    public void updateDetails(String name, Double price, Integer stockQuantity) {
        this.name = name;
        this.price = price;
        this.stockQuantity = stockQuantity;
    }
}
