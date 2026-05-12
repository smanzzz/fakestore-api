package org.example.service;

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

        //instansiera ett tomt objekt av order
        Order order = new Order();

        //Verifiera att user med inloggade username finns, annars kasta ett fel.
        User user = userRepository.findByUsername(username)
                .orElseThrow(()-> new IllegalArgumentException("Användare med det användarnamn existerar inte"));

        //sätt user på order objektet.
        order.setUser(user);

        //skicka över listan från requesten till

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

        //sen sätter vi den nya listan på orderns listfält
        //samt sätter tiden till nu när vi skapar ordern.
        order.setOrderItems(orderItemList);
        order.setCreatedAt(LocalDateTime.now());

        //spara order till databas
        Order savedOrder =  orderRepository.save(order);

        //returnera order och mappa om till dto
        return orderMapper.fromEntity(savedOrder);
    }


    public OrderResponseDTO getOrderById(String username, Long id){

        if (!userRepository.existsByUsername(username)){
            throw new IllegalArgumentException("Användare med det användarnamn existerar inte");
        }

         Order order = orderRepository.findById(id)
                 .orElseThrow(()-> new IllegalArgumentException("Order med det order id existerar ej"));

        return orderMapper.fromEntity(order);
    }


}
