package org.example.mapper;

import org.example.dto.OrderResponseDTO;
import org.example.model.Order;
import org.example.model.User;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

class OrderMapperTest {

    @Test
    void fromEntity_ShouldMapOrderToResponseDTO() {
        OrderMapper orderMapper = new OrderMapper();

        User user = new User();
        user.setId(10L);
        user.setUsername("customer");

        LocalDateTime createdAt = LocalDateTime.now();

        Order order = new Order();
        order.setId(1L);
        order.setUser(user);
        order.setOrderItems(List.of());
        order.setCreatedAt(createdAt);

        OrderResponseDTO result = orderMapper.fromEntity(order);

        assertNotNull(result);
        assertEquals(1L, result.id());
        assertEquals(10L, result.userId());
        assertEquals(createdAt, result.createdAt());
    }
}
