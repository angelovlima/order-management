package com.product_catalog.api.service;
import com.product_catalog.api.exception.DuplicateResourceException;
import com.product_catalog.api.exception.ResourceNotFoundException;
import com.product_catalog.api.model.dto.ProductDTO;
import com.product_catalog.api.model.entity.Product;
import com.product_catalog.api.repository.ProductRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class ProductService {

    private final ProductRepository productRepository;

    @Autowired
    public ProductService(ProductRepository productRepository) {
        this.productRepository = productRepository;
    }

    public ProductDTO save(ProductDTO productDTO) {
        if (productRepository.existsByName(productDTO.name())) {
            throw new DuplicateResourceException("Produto com o nome " + productDTO.name() + " já existe.");
        }

        Product product = new Product();
        product.setName(productDTO.name());
        product.setPrice(productDTO.price());
        product.setStockQuantity(productDTO.stockQuantity());
        product.setCreateDateTime(LocalDateTime.now());

        Product newProduct = productRepository.save(product);
        return toDto(newProduct);
    }

    public ProductDTO findById(Long id) {
        Product product = productRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Produto com o id " + id + " não encontrado."));
        return toDto(product);
    }

    public List<ProductDTO> findAll() {
        return productRepository.findAll()
                .stream()
                .map(this::toDto)
                .collect(Collectors.toList());
    }

    public ProductDTO update(Long id, ProductDTO productDTO) {
        Product existingProduct = productRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Produto com o id " + id + " não encontrado."));

        if (!existingProduct.getName().equals(productDTO.name()) &&
                productRepository.existsByName(productDTO.name())) {
            throw new DuplicateResourceException("Produto com o nome " + productDTO.name() + " já existe.");
        }

        existingProduct.setName(productDTO.name());
        existingProduct.setPrice(productDTO.price());
        existingProduct.setStockQuantity(productDTO.stockQuantity());

        Product updatedProduct = productRepository.save(existingProduct);
        return toDto(updatedProduct);
    }

    public void delete(Long id) {
        if (!productRepository.existsById(id)) {
            throw new ResourceNotFoundException("Produto com o id " + id + " não encontrado.");
        }
        productRepository.deleteById(id);
    }

    private ProductDTO toDto(Product product) {
        return new ProductDTO(
                product.getId(),
                product.getName(),
                product.getPrice(),
                product.getStockQuantity()
        );
    }

    public void validateProductStock(Long productId, Integer requestedQuantity) {
        Product product = productRepository.findById(productId)
                .orElseThrow(() -> new ResourceNotFoundException("Produto com o id " + productId + " não encontrado."));

        if (requestedQuantity > product.getStockQuantity()) {
            throw new IllegalArgumentException("Estoque insuficiente para o produto com o id " + productId);
        }

        deductStock(product, requestedQuantity);
    }

    private void deductStock(Product product, Integer quantityToDeduct) {
        product.setStockQuantity(product.getStockQuantity() - quantityToDeduct);
        productRepository.save(product);
    }
}

