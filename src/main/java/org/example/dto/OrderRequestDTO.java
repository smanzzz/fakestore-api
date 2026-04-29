package org.example.dto;


import org.example.model.OrderItem;
import org.example.model.User;

import java.time.LocalDateTime;
import java.util.List;

public record OrderRequestDTO(Long userId,
                              List<OrderItemDTO> orderItemList
                              ) {
}
