package org.example.service;

import org.example.dto.OrderItemDTO;
import org.example.dto.OrderRequestDTO;
import org.example.dto.OrderResponseDTO;
import org.example.mapper.OrderMapper;
import org.example.model.Order;
import org.example.model.OrderItem;
import org.example.model.User;
import org.example.repository.OrderRepository;
import org.example.repository.UserRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class OrderServiceTest {

    @Mock
    UserRepository userRepository;
    @Mock
    OrderRepository orderRepository;
    @Mock
    OrderMapper orderMapper;

    @InjectMocks
    OrderService orderService;


    @Test
    void shouldCreateOrder(){
        //Arrange
        OrderItemDTO orderItemDTO = new OrderItemDTO(
                1L,
                10);

        List<OrderItemDTO> orderItems = List.of(orderItemDTO);
        OrderRequestDTO requestDTO = new OrderRequestDTO(orderItems);

        User user = new User();
        user.setId(1L);
        user.setUsername("Kalle");

        LocalDateTime createdAt = LocalDateTime.now();
        OrderResponseDTO expectedResponseDTO=  new OrderResponseDTO(
                1L,
                1L,
                orderItems,
                createdAt);

        when(userRepository.findByUsername(user.getUsername())).thenReturn(Optional.of(user));
        when(orderRepository.save(any(Order.class))).thenAnswer(invocation -> invocation.getArgument(0));
        when(orderMapper.fromEntity(any(Order.class))).thenReturn(expectedResponseDTO);
        //Act
        OrderResponseDTO resultRespDTO = orderService.createOrder(user.getUsername(),requestDTO);
        //Assert
        assertEquals(expectedResponseDTO,resultRespDTO);
        ArgumentCaptor<Order> orderCaptor = ArgumentCaptor.forClass(Order.class);

        verify(orderRepository).save(orderCaptor.capture());
        Order capturedOrder = orderCaptor.getValue();
        verify(userRepository).findByUsername(user.getUsername());
        verify(orderMapper).fromEntity(capturedOrder);

        assertSame(user, capturedOrder.getUser());
        assertNotNull(capturedOrder.getCreatedAt());

        assertEquals(1,capturedOrder.getOrderItems().size());

        OrderItem capturedOrderItem = capturedOrder.getOrderItems().getFirst();

        assertEquals(1L, capturedOrderItem.getProductId());
        assertEquals(10, capturedOrderItem.getAmountOfProduct());
        assertSame(capturedOrder, capturedOrderItem.getOrder());
    }



    @Test
    void shouldThrowIllegalArgumentException(){
        //Arrange
        OrderItemDTO orderItemDTO = new OrderItemDTO(
                1L,
                10);

        List<OrderItemDTO> orderItems = List.of(orderItemDTO);

        OrderRequestDTO orderRequestDTO = new OrderRequestDTO(orderItems);

        User user = new User();
        user.setUsername("Kalle");

        when(userRepository.findByUsername(user.getUsername())).thenReturn(Optional.empty());
        //Act
        //Assert
        assertThrows(IllegalArgumentException.class,
                () -> {
                    orderService.createOrder(user.getUsername(),orderRequestDTO);
                });
    }


}
