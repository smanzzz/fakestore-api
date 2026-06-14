package org.example.dto;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

public record OrderItemDTO(
        @NotNull(message = "Produkt-id måste anges.")
        Long productId,
        @NotNull(message = "Antal måste anges.")
        @Positive(message = "Antal måste vara större än 0.")
        Integer amountOfProduct) {
}
