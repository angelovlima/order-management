package com.order_processing.api.client;

import com.order_processing.api.exception.ProductValidationException;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.reactive.function.client.WebClientResponseException;

@Component
public class ProductClient {

    private final WebClient webClient;

    public ProductClient(WebClient.Builder webClientBuilder) {
        this.webClient = webClientBuilder.baseUrl("http://localhost:8082/product").build();
    }

    public void validateProduct(Long productId, Integer requiredQuantity) {
        try {
            // Chamada ao endpoint para validação do produto e estoque
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
