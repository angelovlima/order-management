package com.order_processing.api.order.gateway.impl;

import com.order_processing.api.order.exception.CustomerNotFoundException;
import com.order_processing.api.order.gateway.CustomerGateway;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.reactive.function.client.WebClientResponseException;

@Component
public class CustomerClient implements CustomerGateway {

    private final WebClient webClient;

    public CustomerClient(WebClient.Builder webClientBuilder, @Value("${api.customer.url}") String customerUrl) {
        this.webClient = webClientBuilder.baseUrl(customerUrl).build();
    }

    public void validateCustomer(Long customerId) {
        try {
            webClient.get()
                    .uri("/{id}/exists", customerId)
                    .retrieve()
                    .toBodilessEntity()
                    .block();
        } catch (WebClientResponseException.NotFound e) {
            throw new CustomerNotFoundException("Cliente com ID " + customerId + " não encontrado.");
        }
    }
}
