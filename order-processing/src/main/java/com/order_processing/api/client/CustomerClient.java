package com.order_processing.api.client;

import com.order_processing.api.exception.CustomerNotFoundException;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.reactive.function.client.WebClientResponseException;

@Component
public class CustomerClient {

    private final WebClient webClient;

    public CustomerClient(WebClient.Builder webClientBuilder) {
        this.webClient = webClientBuilder.baseUrl("http://localhost:8081/customer").build();
    }

    public void validateCustomer(Long customerId) {
        try {
            // Chamada ao endpoint do microserviço customer-management
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
