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

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatExceptionOfType;
import static org.mockito.Mockito.*;

class FindProductByIdUseCaseTest {

    @InjectMocks
    private FindProductByIdUseCase findProductByIdUseCase;

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
    void shouldReturnProductWhenIdExists() {
        Long productId = 1L;
        Product expectedProduct = new Product(productId, "Notebook Dell Inspiron 15", 3500.50, 25);
        when(productGateway.findById(productId)).thenReturn(Optional.of(expectedProduct));

        Product result = findProductByIdUseCase.execute(productId);

        assertThat(result).isNotNull();
        assertThat(result.getId()).isEqualTo(expectedProduct.getId());
        assertThat(result.getName()).isEqualTo(expectedProduct.getName());
        assertThat(result.getPrice()).isEqualTo(expectedProduct.getPrice());
        assertThat(result.getStockQuantity()).isEqualTo(expectedProduct.getStockQuantity());

        verify(productGateway).findById(productId);
        verifyNoMoreInteractions(productGateway);
    }

    @Test
    void shouldThrowResourceNotFoundExceptionWhenIdDoesNotExist() {
        Long productId = 999L;
        when(productGateway.findById(productId)).thenReturn(Optional.empty());

        assertThatExceptionOfType(ResourceNotFoundException.class)
                .isThrownBy(() -> findProductByIdUseCase.execute(productId))
                .withMessage("Produto com o id " + productId + " não encontrado.");

        verify(productGateway).findById(productId);
        verifyNoMoreInteractions(productGateway);
    }
}
