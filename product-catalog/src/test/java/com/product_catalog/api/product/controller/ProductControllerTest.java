package com.product_catalog.api.product.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.product_catalog.api.product.domain.Product;
import com.product_catalog.api.product.exception.ResourceNotFoundException;
import com.product_catalog.api.product.usecase.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Arrays;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(ProductController.class)
class ProductControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private CreateProductUseCase createProductUseCase;

    @MockBean
    private DeleteProductUseCase deleteProductUseCase;

    @MockBean
    private FindProductByIdUseCase findProductByIdUseCase;

    @MockBean
    private UpdateProductUseCase updateProductUseCase;

    @MockBean
    private ValidateProductStockUseCase validateProductStockUseCase;

    @MockBean
    private FindAllProductsUseCase findAllProductsUseCase;

    private Product product;

    private final ObjectMapper objectMapper = new ObjectMapper();

    @BeforeEach
    void setUp() {
        product = new Product(1L, "Notebook Dell", 3500.0, 10);
    }

    @Test
    void shouldSaveProductSuccessfully() throws Exception {
        when(createProductUseCase.execute(any(Product.class))).thenReturn(product);

        mockMvc.perform(post("/product")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(product)))
                .andExpect(status().isCreated())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.id").value(product.getId()))
                .andExpect(jsonPath("$.name").value(product.getName()))
                .andExpect(jsonPath("$.price").value(product.getPrice()));

        verify(createProductUseCase).execute(any(Product.class));
        verifyNoMoreInteractions(createProductUseCase);
    }

    @Test
    void shouldUpdateProductSuccessfully() throws Exception {
        when(updateProductUseCase.execute(eq(product.getId()), any(Product.class))).thenReturn(product);

        mockMvc.perform(put("/product/{id}", product.getId())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(product)))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.id").value(product.getId()))
                .andExpect(jsonPath("$.name").value(product.getName()));

        verify(updateProductUseCase).execute(eq(product.getId()), any(Product.class));
        verifyNoMoreInteractions(updateProductUseCase);
    }

    @Test
    void shouldDeleteProductSuccessfully() throws Exception {
        mockMvc.perform(delete("/product/{id}", product.getId()))
                .andExpect(status().isNoContent());

        verify(deleteProductUseCase).execute(product.getId());
        verifyNoMoreInteractions(deleteProductUseCase);
    }

    @Test
    void shouldReturnNotFoundWhenDeletingNonExistingProduct() throws Exception {
        doThrow(new ResourceNotFoundException("Produto com o id " + product.getId() + " não encontrado."))
                .when(deleteProductUseCase).execute(product.getId());

        mockMvc.perform(delete("/product/{id}", product.getId()))
                .andExpect(status().isNotFound())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON));

        verify(deleteProductUseCase).execute(product.getId());
        verifyNoMoreInteractions(deleteProductUseCase);
    }

    @Test
    void shouldGetProductByIdSuccessfully() throws Exception {
        when(findProductByIdUseCase.execute(product.getId())).thenReturn(product);

        mockMvc.perform(get("/product/{id}", product.getId())
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.id").value(product.getId()))
                .andExpect(jsonPath("$.name").value(product.getName()));

        verify(findProductByIdUseCase).execute(product.getId());
        verifyNoMoreInteractions(findProductByIdUseCase);
    }

    @Test
    void shouldListAllProductsSuccessfully() throws Exception {
        when(findAllProductsUseCase.execute()).thenReturn(Arrays.asList(product));

        mockMvc.perform(get("/product")
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$[0].id").value(product.getId()))
                .andExpect(jsonPath("$[0].name").value(product.getName()));

        verify(findAllProductsUseCase).execute();
        verifyNoMoreInteractions(findAllProductsUseCase);
    }

    @Test
    void shouldReturnNotFoundWhenProductDoesNotExist() throws Exception {
        doThrow(new ResourceNotFoundException("Produto com o id " + product.getId() + " não encontrado."))
                .when(findProductByIdUseCase).execute(product.getId());

        mockMvc.perform(get("/product/{id}", product.getId()))
                .andExpect(status().isNotFound())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON));

        verify(findProductByIdUseCase).execute(product.getId());
        verifyNoMoreInteractions(findProductByIdUseCase);
    }
}

