package com.product_catalog.api.product.usecase;

import com.product_catalog.api.product.domain.Product;
import com.product_catalog.api.product.gateway.ProductGateway;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.Arrays;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.*;

class FindAllProductsUseCaseTest {

    @InjectMocks
    private FindAllProductsUseCase findAllProductsUseCase;

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
    void shouldReturnAllProducts() {
        List<Product> expectedProducts = Arrays.asList(
                new Product(1L, "Product A", 100.0, 10),
                new Product(2L, "Product B", 200.0, 20)
        );
        when(productGateway.findAll()).thenReturn(expectedProducts);

        List<Product> result = findAllProductsUseCase.execute();

        assertThat(result)
                .isNotNull()
                .isNotEmpty()
                .hasSize(expectedProducts.size())
                .containsAll(expectedProducts);

        verify(productGateway).findAll();
        verifyNoMoreInteractions(productGateway);
    }

    @Test
    void shouldReturnEmptyListWhenNoProductsExist() {
        when(productGateway.findAll()).thenReturn(List.of());

        List<Product> result = findAllProductsUseCase.execute();

        assertThat(result).isNotNull().isEmpty();

        verify(productGateway).findAll();
        verifyNoMoreInteractions(productGateway);
    }
}
