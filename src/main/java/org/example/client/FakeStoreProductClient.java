package org.example.client;

import org.example.dto.FakeStoreProductResponseDTO;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestClient;

import java.util.List;

@Component
public class FakeStoreProductClient {

    private final RestClient restClient;

    public FakeStoreProductClient() {
        this.restClient = RestClient.builder()
                .baseUrl("https://fakestoreapi.com")
                .build();
    }

    public List<FakeStoreProductResponseDTO> getAllProducts() {
        return restClient.get()
                .uri("/products")
                .retrieve()
                .body(new ParameterizedTypeReference<>() {});
    }

    public FakeStoreProductResponseDTO getProductById(Long id) {
        return restClient.get()
                .uri("/products/{id}", id)
                .retrieve()
                .body(FakeStoreProductResponseDTO.class);
    }
}
