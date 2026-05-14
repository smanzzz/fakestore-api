package org.example.mapper;

import org.example.dto.OrderResponseDTO;
import org.example.model.Order;
import org.example.model.OrderItem;
import org.example.model.User;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

public class OrderMapperTest {


    @Test
    public void orderMapperTestFromEntity(){
        //Arrange
        // skapa ett objekt av typen Order
        //skicka in order in i Ordermappern och spara det i en orderResponseDTO.
        OrderMapper orderMapper = new OrderMapper();

        User user = new User();
        user.setId(10L);
        user.setUsername("Habib");

        OrderItem orderItem = new OrderItem();

        orderItem.setProductId(1L);
        orderItem.setAmountOfProduct(2);

        List<OrderItem> items = List.of();

        LocalDateTime createdAt = LocalDateTime.now();

        Order order = new Order();

        order.setId(1L);
        order.setUser(user);
        order.setOrderItems(items);
        order.setCreatedAt(createdAt);

        //Act
        OrderResponseDTO resultRespDTO = orderMapper.fromEntity(order);

        //Assert
        assertNotNull(resultRespDTO);
        assertEquals(1L, resultRespDTO.id());
        assertEquals(10L, resultRespDTO.userId());
        assertEquals(createdAt, resultRespDTO.createdAt());



    }
}
