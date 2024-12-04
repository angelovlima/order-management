package com.order_processing.api.order.gateway.impl;

import com.order_processing.api.order.exception.ProductValidationException;
import okhttp3.mockwebserver.MockResponse;
import okhttp3.mockwebserver.MockWebServer;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.web.reactive.function.client.WebClient;

import static org.junit.jupiter.api.Assertions.assertThrows;

class ProductClientTest {

    private MockWebServer mockWebServer;
    private ProductClient productClient;

    @BeforeEach
    void setUp() throws Exception {
        mockWebServer = new MockWebServer();
        mockWebServer.start();

        String baseUrl = mockWebServer.url("/").toString();
        WebClient.Builder webClientBuilder = WebClient.builder();

        productClient = new ProductClient(webClientBuilder, baseUrl);
    }

    @AfterEach
    void tearDown() throws Exception {
        mockWebServer.shutdown();
    }

    @Test
    void shouldValidateProductSuccessfully() {
        mockWebServer.enqueue(new MockResponse().setResponseCode(200));

        productClient.validateProduct(1L, 10);
    }

    @Test
    void shouldThrowProductValidationExceptionWhenProductNotFound() {
        mockWebServer.enqueue(new MockResponse().setResponseCode(404));

        assertThrows(ProductValidationException.class, () -> productClient.validateProduct(1L, 10));
    }

    @Test
    void shouldThrowProductValidationExceptionWhenStockIsInsufficient() {
        mockWebServer.enqueue(new MockResponse().setResponseCode(400));

        assertThrows(ProductValidationException.class, () -> productClient.validateProduct(1L, 10));
    }
}
