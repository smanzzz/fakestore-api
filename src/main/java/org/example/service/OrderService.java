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
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class OrderService {

    private final UserRepository userRepository;
    private final OrderRepository orderRepository;
    private final OrderMapper orderMapper;

    public OrderService(UserRepository userRepository, OrderRepository orderRepository, OrderMapper orderMapper){
        this.userRepository=userRepository;
        this.orderRepository=orderRepository;
        this.orderMapper=orderMapper;

    }


    public OrderResponseDTO createOrder(String username, OrderRequestDTO requestDTO){

        Order order = new Order();

        User user = userRepository.findByUsername(username)
                .orElseThrow(()-> new IllegalArgumentException("Användare med det användarnamn existerar inte"));

        order.setUser(user);

        List<OrderItem> orderItemList = requestDTO.orderItemList()
                .stream()
                .map(orderItemDTO -> {
                    OrderItem item = new OrderItem();
                    item.setProductId(orderItemDTO.productId());
                    item.setAmountOfProduct(orderItemDTO.amountOfProduct());
                    item.setOrder(order);
                    return item;
                        })
                .toList();

        order.setOrderItems(orderItemList);
        order.setCreatedAt(LocalDateTime.now());

        Order savedOrder =  orderRepository.save(order);

        return orderMapper.fromEntity(savedOrder);
    }


    public OrderResponseDTO getOrderById(String username, Long orderId){


        return ;
    }


}
