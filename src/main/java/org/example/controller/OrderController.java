package org.example.controller;

import com.fasterxml.classmate.members.ResolvedParameterizedMember;
import org.example.dto.OrderRequestDTO;
import org.example.dto.OrderResponseDTO;
import org.example.service.OrderService;
import org.example.service.UserService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/orders")
public class OrderController {

    private final OrderService orderService;


    public OrderController (OrderService orderService){
        this.orderService=orderService;

    }



    @PostMapping("/create")
    public ResponseEntity<OrderResponseDTO> createOrder (@AuthenticationPrincipal String username, @RequestBody OrderRequestDTO requestDTO){
      OrderResponseDTO respDTO =   orderService.createOrder(username, requestDTO);

                return ResponseEntity.ok(respDTO);
    }
}
