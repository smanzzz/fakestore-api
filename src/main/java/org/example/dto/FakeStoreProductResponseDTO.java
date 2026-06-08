package org.example.dto;

import java.math.BigDecimal;

public record FakeStoreProductResponseDTO(
        Long id,
        String title,
        BigDecimal price,
        String description,
        String category,
        String image) {
}
