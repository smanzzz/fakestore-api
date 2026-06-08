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
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class OrderServiceTest {

    @Mock
    private UserRepository userRepository;

    @Mock
    private OrderRepository orderRepository;

    @Mock
    private OrderMapper orderMapper;

    @InjectMocks
    private OrderService orderService;

    private User user;
    private OrderRequestDTO orderRequestDTO;
    private Order order;
    private OrderResponseDTO orderResponseDTO;

    @BeforeEach
    void setUp() {
        user = new User();
        user.setId(1L);
        user.setUsername("customer");

        OrderItemDTO itemDTO = new OrderItemDTO(100L, 2);
        orderRequestDTO = new OrderRequestDTO(List.of(itemDTO));

        order = new Order();
        order.setId(1L);
        order.setUser(user);

        OrderItem orderItem = new OrderItem();
        orderItem.setId(1L);
        orderItem.setProductId(100L);
        orderItem.setAmountOfProduct(2);
        orderItem.setOrder(order);

        order.setOrderItems(List.of(orderItem));
        order.setCreatedAt(LocalDateTime.now());

        orderResponseDTO = new OrderResponseDTO(1L, 1L, List.of(), LocalDateTime.now());
    }

    @Test
    void createOrder_Success() {
        when(userRepository.findByUsername("customer")).thenReturn(Optional.of(user));
        when(orderRepository.save(any(Order.class))).thenReturn(order);
        when(orderMapper.fromEntity(order)).thenReturn(orderResponseDTO);

        OrderResponseDTO result = orderService.createOrder("customer", orderRequestDTO);

        assertNotNull(result);
        assertEquals(1L, result.id());
        assertEquals(1L, result.userId());
        verify(userRepository, times(1)).findByUsername("customer");
        verify(orderRepository, times(1)).save(any(Order.class));
    }

    @Test
    void createOrder_ThrowsExceptionWhenUserNotFound() {
        when(userRepository.findByUsername("unknown")).thenReturn(Optional.empty());

        IllegalArgumentException exception = assertThrows(
                IllegalArgumentException.class,
                () -> orderService.createOrder("unknown", orderRequestDTO));

        assertEquals("Användaren finns inte.", exception.getMessage());
        verify(orderRepository, never()).save(any(Order.class));
    }

    @Test
    void getOrderById_Success() {
        when(userRepository.existsByUsername("customer")).thenReturn(true);
        when(orderRepository.findByIdAndUserUsername(1L, "customer")).thenReturn(Optional.of(order));
        when(orderMapper.fromEntity(order)).thenReturn(orderResponseDTO);

        OrderResponseDTO result = orderService.getOrderById("customer", 1L);

        assertNotNull(result);
        assertEquals(1L, result.id());
        verify(orderRepository, times(1)).findByIdAndUserUsername(1L, "customer");
    }

    @Test
    void getOrderById_ThrowsExceptionWhenUserNotFound() {
        when(userRepository.existsByUsername("unknown")).thenReturn(false);

        IllegalArgumentException exception = assertThrows(
                IllegalArgumentException.class,
                () -> orderService.getOrderById("unknown", 1L));

        assertEquals("Användaren finns inte.", exception.getMessage());
    }

    @Test
    void getOrderById_ThrowsExceptionWhenOrderNotFound() {
        when(userRepository.existsByUsername("customer")).thenReturn(true);
        when(orderRepository.findByIdAndUserUsername(1L, "customer")).thenReturn(Optional.empty());

        IllegalArgumentException exception = assertThrows(
                IllegalArgumentException.class,
                () -> orderService.getOrderById("customer", 1L));

        assertEquals("Ordern finns inte.", exception.getMessage());
    }
}
