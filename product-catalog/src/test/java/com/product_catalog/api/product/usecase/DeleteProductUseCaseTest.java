package com.product_catalog.api.product.usecase;

import com.product_catalog.api.product.exception.ResourceNotFoundException;
import com.product_catalog.api.product.gateway.ProductGateway;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import static org.assertj.core.api.Assertions.assertThatExceptionOfType;
import static org.mockito.Mockito.*;

class DeleteProductUseCaseTest {

    @InjectMocks
    private DeleteProductUseCase deleteProductUseCase;

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
    void shouldDeleteProductWhenIdExists() {
        Long productId = 1L;
        when(productGateway.existsById(productId)).thenReturn(true);

        deleteProductUseCase.execute(productId);

        verify(productGateway).existsById(productId);
        verify(productGateway).deleteById(productId);
        verifyNoMoreInteractions(productGateway);
    }

    @Test
    void shouldThrowResourceNotFoundExceptionWhenIdDoesNotExist() {
        Long productId = 1L;
        when(productGateway.existsById(productId)).thenReturn(false);

        assertThatExceptionOfType(ResourceNotFoundException.class)
                .isThrownBy(() -> deleteProductUseCase.execute(productId))
                .withMessage("Produto com o id " + productId + " não encontrado.");

        verify(productGateway).existsById(productId);
        verifyNoMoreInteractions(productGateway);
    }
}
