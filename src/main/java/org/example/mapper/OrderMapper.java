package org.example.mapper;

import org.example.dto.OrderItemDTO;
import org.example.dto.OrderResponseDTO;
import org.example.model.Order;
import org.example.model.OrderItem;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class OrderMapper {

    public OrderResponseDTO fromEntity(Order order){

        List<OrderItemDTO> items = order.getOrderItems()
                .stream()
                .map(item -> new OrderItemDTO(
                        item.getProductId(),
                        item.getAmountOfProduct()))
                .toList();

         OrderResponseDTO orderResponseDTO = new OrderResponseDTO(
                 order.getId(),
                 order.getUser().getId(),
                 items,
                 order.getCreatedAt());

         return orderResponseDTO;
    }


}
