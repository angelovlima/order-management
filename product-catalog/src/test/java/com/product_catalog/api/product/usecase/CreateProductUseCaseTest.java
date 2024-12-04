package com.product_catalog.api.product.usecase;

import com.product_catalog.api.product.domain.Product;
import com.product_catalog.api.product.exception.DuplicateResourceException;
import com.product_catalog.api.product.gateway.ProductGateway;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatExceptionOfType;
import static org.mockito.Mockito.*;

class CreateProductUseCaseTest {

    @InjectMocks
    private CreateProductUseCase createProductUseCase;

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
    void shouldCreateProductWhenNameDoesNotExist() {
        Product product = new Product(null, "Notebook Dell Inspiron 15", 3500.50, 25);
        Product savedProduct = new Product(1L, "Notebook Dell Inspiron 15", 3500.50, 25);

        when(productGateway.existsByName(product.getName())).thenReturn(false);
        when(productGateway.save(product)).thenReturn(savedProduct);

        Product result = createProductUseCase.execute(product);

        assertThat(result).isNotNull();
        assertThat(result.getId()).isEqualTo(savedProduct.getId());
        assertThat(result.getName()).isEqualTo(savedProduct.getName());
        assertThat(result.getPrice()).isEqualTo(savedProduct.getPrice());
        assertThat(result.getStockQuantity()).isEqualTo(savedProduct.getStockQuantity());

        verify(productGateway).existsByName(product.getName());
        verify(productGateway).save(product);
        verifyNoMoreInteractions(productGateway);
    }

    @Test
    void shouldThrowDuplicateResourceExceptionWhenNameAlreadyExists() {
        Product product = new Product(null, "Notebook Dell Inspiron 15", 3500.50, 25);
        when(productGateway.existsByName(product.getName())).thenReturn(true);

        assertThatExceptionOfType(DuplicateResourceException.class)
                .isThrownBy(() -> createProductUseCase.execute(product))
                .withMessage("Produto com o nome " + product.getName() + " já existe.");

        verify(productGateway).existsByName(product.getName());
        verifyNoMoreInteractions(productGateway);
    }
}
