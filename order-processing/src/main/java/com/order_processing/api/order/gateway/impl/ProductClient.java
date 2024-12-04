package com.order_processing.api.order.gateway.impl;

import com.order_processing.api.order.exception.ProductValidationException;
import com.order_processing.api.order.gateway.ProductGateway;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.reactive.function.client.WebClientResponseException;

@Component
public class ProductClient implements ProductGateway {

    private final WebClient webClient;

    public ProductClient(WebClient.Builder webClientBuilder, @Value("${api.product.url}") String productUrl) {
        this.webClient = webClientBuilder.baseUrl(productUrl).build();
    }

    public void validateProduct(Long productId, Integer requiredQuantity) {
        try {
            webClient.get()
                    .uri(uriBuilder -> uriBuilder
                            .path("/{id}/validate")
                            .queryParam("stockQuantity", requiredQuantity)
                            .build(productId))
                    .retrieve()
                    .toBodilessEntity()
                    .block();
        } catch (WebClientResponseException.NotFound e) {
            throw new ProductValidationException("Produto com ID " + productId + " não encontrado.");
        } catch (WebClientResponseException.BadRequest e) {
            throw new ProductValidationException("Estoque insuficiente para o produto com ID " + productId + ".");
        }
    }
}
