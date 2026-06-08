package org.example.dto;

import java.time.LocalDateTime;
import java.util.List;

public record OrderResponseDTO(
        Long id,
        Long userId,
        List<OrderItemDTO> orderItemDTOList,
        LocalDateTime createdAt) {
}
