package org.example.dto;


import org.example.model.OrderItem;
import org.example.model.User;

import java.time.LocalDateTime;
import java.util.List;

public record OrderRequestDTO(User user,
                              List<OrderItem> orderItemList,
                              LocalDateTime createdAt) {
}
