package com.product_catalog.api.product.usecase;

import com.product_catalog.api.product.domain.Product;
import com.product_catalog.api.product.exception.DuplicateResourceException;
import com.product_catalog.api.product.exception.ResourceNotFoundException;
import com.product_catalog.api.product.gateway.ProductGateway;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatExceptionOfType;
import static org.mockito.Mockito.*;

class UpdateProductUseCaseTest {

    @InjectMocks
    private UpdateProductUseCase updateProductUseCase;

    @Mock
    private ProductGateway productGateway;

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
    void shouldUpdateProductWhenValidDataProvided() {
        Long productId = 1L;
        Product existingProduct = new Product(productId, "Notebook Dell Inspiron 15", 3500.50, 25);
        Product updatedProduct = new Product(null, "Notebook Dell Inspiron 16", 3700.00, 30);

        when(productGateway.findById(productId)).thenReturn(Optional.of(existingProduct));
        when(productGateway.existsByName(updatedProduct.getName())).thenReturn(false);
        when(productGateway.save(existingProduct)).thenReturn(existingProduct);

        Product result = updateProductUseCase.execute(productId, updatedProduct);

        assertThat(result).isNotNull();
        assertThat(result.getName()).isEqualTo(updatedProduct.getName());
        assertThat(result.getPrice()).isEqualTo(updatedProduct.getPrice());
        assertThat(result.getStockQuantity()).isEqualTo(updatedProduct.getStockQuantity());

        verify(productGateway).findById(productId);
        verify(productGateway).existsByName(updatedProduct.getName());
        verify(productGateway).save(existingProduct);
        verifyNoMoreInteractions(productGateway);
    }

    @Test
    void shouldThrowResourceNotFoundExceptionWhenProductIdDoesNotExist() {
        Long productId = 999L;
        Product updatedProduct = new Product(null, "Notebook Dell Inspiron 16", 3700.00, 30);

        when(productGateway.findById(productId)).thenReturn(Optional.empty());

        assertThatExceptionOfType(ResourceNotFoundException.class)
                .isThrownBy(() -> updateProductUseCase.execute(productId, updatedProduct))
                .withMessage("Produto com o id " + productId + " não encontrado.");

        verify(productGateway).findById(productId);
        verifyNoMoreInteractions(productGateway);
    }

    @Test
    void shouldThrowDuplicateResourceExceptionWhenProductNameAlreadyExists() {
        Long productId = 1L;
        Product existingProduct = new Product(productId, "Notebook Dell Inspiron 15", 3500.50, 25);
        Product updatedProduct = new Product(null, "Notebook Dell Inspiron 16", 3700.00, 30);

        when(productGateway.findById(productId)).thenReturn(Optional.of(existingProduct));
        when(productGateway.existsByName(updatedProduct.getName())).thenReturn(true);

        assertThatExceptionOfType(DuplicateResourceException.class)
                .isThrownBy(() -> updateProductUseCase.execute(productId, updatedProduct))
                .withMessage("Produto com o nome " + updatedProduct.getName() + " já existe.");

        verify(productGateway).findById(productId);
        verify(productGateway).existsByName(updatedProduct.getName());
        verifyNoMoreInteractions(productGateway);
    }
}
