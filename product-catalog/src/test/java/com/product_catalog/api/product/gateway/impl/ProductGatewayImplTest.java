package com.product_catalog.api.product.gateway.impl;

import com.product_catalog.api.config.db.entity.ProductEntity;
import com.product_catalog.api.config.db.repository.ProductRepository;
import com.product_catalog.api.product.domain.Product;
import com.product_catalog.api.product.exception.ResourceNotFoundException;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.Mockito.*;

class ProductGatewayImplTest {

    @InjectMocks
    private ProductGatewayImpl productGateway;

    @Mock
    private ProductRepository productRepository;

    private AutoCloseable openMocks;

    @BeforeEach
    void setUp() {
        openMocks = MockitoAnnotations.openMocks(this);
    }

    @AfterEach
    void tearDown() throws Exception {
        openMocks.close();
    }

    @Test
    void shouldSaveProductSuccessfully() {
        Product product = new Product(1L, "Notebook Dell", 3500.0, 10);
        ProductEntity productEntity = new ProductEntity(1L, "Notebook Dell", 3500.0, 10, LocalDateTime.now());

        when(productRepository.save(any(ProductEntity.class))).thenReturn(productEntity);

        Product savedProduct = productGateway.save(product);

        assertThat(savedProduct)
                .isNotNull()
                .extracting("id", "name", "price", "stockQuantity")
                .containsExactly(1L, "Notebook Dell", 3500.0, 10);

        verify(productRepository).save(any(ProductEntity.class));
        verifyNoMoreInteractions(productRepository);
    }

    @Test
    void shouldReturnProductWhenFindByIdExists() {
        Long productId = 1L;
        ProductEntity productEntity = new ProductEntity(productId, "Notebook Dell", 3500.0, 10, LocalDateTime.now());

        when(productRepository.findById(productId)).thenReturn(Optional.of(productEntity));

        Optional<Product> result = productGateway.findById(productId);

        assertThat(result).isPresent()
                .get()
                .extracting("id", "name", "price", "stockQuantity")
                .containsExactly(1L, "Notebook Dell", 3500.0, 10);

        verify(productRepository).findById(productId);
        verifyNoMoreInteractions(productRepository);
    }

    @Test
    void shouldReturnEmptyWhenFindByIdDoesNotExist() {
        Long productId = 1L;

        when(productRepository.findById(productId)).thenReturn(Optional.empty());

        Optional<Product> result = productGateway.findById(productId);

        assertThat(result).isEmpty();

        verify(productRepository).findById(productId);
        verifyNoMoreInteractions(productRepository);
    }

    @Test
    void shouldReturnAllProducts() {
        ProductEntity product1 = new ProductEntity(1L, "Notebook Dell", 3500.0, 10, LocalDateTime.now());
        ProductEntity product2 = new ProductEntity(2L, "Monitor LG", 1500.0, 5, LocalDateTime.now());

        when(productRepository.findAll()).thenReturn(List.of(product1, product2));

        List<Product> products = productGateway.findAll();

        assertThat(products).isNotNull().hasSize(2);
        assertThat(products)
                .extracting("id", "name", "price", "stockQuantity")
                .containsExactly(
                        tuple(1L, "Notebook Dell", 3500.0, 10),
                        tuple(2L, "Monitor LG", 1500.0, 5)
                );

        verify(productRepository).findAll();
        verifyNoMoreInteractions(productRepository);
    }

    @Test
    void shouldReturnTrueWhenExistsByName() {
        String productName = "Notebook Dell";

        when(productRepository.existsByName(productName)).thenReturn(true);

        boolean exists = productGateway.existsByName(productName);

        assertThat(exists).isTrue();

        verify(productRepository).existsByName(productName);
        verifyNoMoreInteractions(productRepository);
    }

    @Test
    void shouldReturnTrueWhenExistsById() {
        Long productId = 1L;

        when(productRepository.existsById(productId)).thenReturn(true);

        boolean exists = productGateway.existsById(productId);

        assertThat(exists).isTrue();

        verify(productRepository).existsById(productId);
        verifyNoMoreInteractions(productRepository);
    }

    @Test
    void shouldDeleteProductById() {
        Long productId = 1L;

        doNothing().when(productRepository).deleteById(productId);

        productGateway.deleteById(productId);

        verify(productRepository).deleteById(productId);
        verifyNoMoreInteractions(productRepository);
    }

    @Test
    void shouldUpdateStockSuccessfully() {
        Long productId = 1L;
        int newStock = 8;
        ProductEntity productEntity = new ProductEntity(productId, "Notebook Dell", 3500.0, 10, LocalDateTime.now());

        when(productRepository.findById(productId)).thenReturn(Optional.of(productEntity));
        when(productRepository.save(any(ProductEntity.class))).thenReturn(productEntity);

        productGateway.updateStock(productId, newStock);

        verify(productRepository).findById(productId);
        verify(productRepository).save(any(ProductEntity.class));
        verifyNoMoreInteractions(productRepository);
    }

    @Test
    void shouldThrowResourceNotFoundExceptionWhenUpdatingStockOfNonExistingProduct() {
        Long productId = 1L;
        int newStock = 8;

        when(productRepository.findById(productId)).thenReturn(Optional.empty());

        assertThatExceptionOfType(ResourceNotFoundException.class)
                .isThrownBy(() -> productGateway.updateStock(productId, newStock))
                .withMessage("Produto com o id " + productId + " não encontrado.");

        verify(productRepository).findById(productId);
        verifyNoMoreInteractions(productRepository);
    }
}
