package com.product_catalog.api.product.usecase;

import com.product_catalog.api.product.domain.Product;
import com.product_catalog.api.product.exception.ResourceNotFoundException;
import com.product_catalog.api.product.gateway.ProductGateway;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThatExceptionOfType;
import static org.mockito.Mockito.*;

class ValidateProductStockUseCaseTest {

    @InjectMocks
    private ValidateProductStockUseCase validateProductStockUseCase;

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
    void shouldValidateStockSuccessfully() {
        Long productId = 1L;
        int availableStock = 10;
        int requestedQuantity = 5;

        Product product = new Product(productId, "Notebook Dell", 3500.0, availableStock);
        when(productGateway.findById(productId)).thenReturn(Optional.of(product));
        validateProductStockUseCase.execute(productId, requestedQuantity);

        verify(productGateway).findById(productId);
        verify(productGateway).updateStock(productId, availableStock - requestedQuantity);
        verifyNoMoreInteractions(productGateway);
    }

    @Test
    void shouldThrowResourceNotFoundExceptionWhenProductDoesNotExist() {
        Long productId = 1L;
        int requestedQuantity = 5;

        when(productGateway.findById(productId)).thenReturn(Optional.empty());

        assertThatExceptionOfType(ResourceNotFoundException.class)
                .isThrownBy(() -> validateProductStockUseCase.execute(productId, requestedQuantity))
                .withMessage("Produto com o id " + productId + " não encontrado.");

        verify(productGateway).findById(productId);
        verifyNoMoreInteractions(productGateway);
    }

    @Test
    void shouldThrowIllegalArgumentExceptionWhenRequestedQuantityExceedsStock() {
        Long productId = 1L;
        int availableStock = 5;
        int requestedQuantity = 10;

        Product product = new Product(productId, "Notebook Dell", 3500.0, availableStock);
        when(productGateway.findById(productId)).thenReturn(Optional.of(product));

        assertThatExceptionOfType(IllegalArgumentException.class)
                .isThrownBy(() -> validateProductStockUseCase.execute(productId, requestedQuantity))
                .withMessage("Estoque insuficiente para o produto com o id " + productId);

        verify(productGateway).findById(productId);
        verifyNoMoreInteractions(productGateway);
    }
}
