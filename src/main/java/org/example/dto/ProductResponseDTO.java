package org.example.dto;

public record ProductResponseDTO(
        int id,
        String title,
        double price,
        String description,
        String category,
        String image,
        RatingResponseDTO ratingRespDTO
) {
}
