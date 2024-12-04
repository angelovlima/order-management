package com.order_processing.api.order.gateway.impl;

import com.order_processing.api.order.exception.CustomerNotFoundException;
import okhttp3.mockwebserver.MockResponse;
import okhttp3.mockwebserver.MockWebServer;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.web.reactive.function.client.WebClient;

import static org.junit.jupiter.api.Assertions.assertThrows;

class CustomerClientTest {

    private MockWebServer mockWebServer;
    private CustomerClient customerClient;

    @BeforeEach
    void setUp() throws Exception {
        mockWebServer = new MockWebServer();
        mockWebServer.start();

        String baseUrl = mockWebServer.url("/").toString();
        WebClient.Builder webClientBuilder = WebClient.builder();

        customerClient = new CustomerClient(webClientBuilder, baseUrl);
    }

    @AfterEach
    void tearDown() throws Exception {
        mockWebServer.shutdown();
    }

    @Test
    void shouldValidateCustomerSuccessfully() {
        mockWebServer.enqueue(new MockResponse().setResponseCode(200));

        customerClient.validateCustomer(1L);
    }

    @Test
    void shouldThrowCustomerNotFoundExceptionWhenCustomerDoesNotExist() {
        mockWebServer.enqueue(new MockResponse().setResponseCode(404));

        assertThrows(CustomerNotFoundException.class, () -> customerClient.validateCustomer(1L));
    }
}
