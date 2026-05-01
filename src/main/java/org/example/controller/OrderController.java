package org.example.controller;

import com.fasterxml.classmate.members.ResolvedParameterizedMember;
import org.example.dto.OrderRequestDTO;
import org.example.dto.OrderResponseDTO;
import org.example.service.OrderService;
import org.example.service.UserService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

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

    @GetMapping("/{id}")
    public ResponseEntity<OrderResponseDTO> getOrderById(@AuthenticationPrincipal String username, @PathVariable Long id){

        OrderResponseDTO respDTO = orderService.getOrderById(username, id);

        return ResponseEntity.ok(respDTO);
    }

}
