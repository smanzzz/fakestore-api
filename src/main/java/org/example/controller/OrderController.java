package org.example.controller;

import com.fasterxml.classmate.members.ResolvedParameterizedMember;
import org.example.service.OrderService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/orders")
public class OrderController {

    private final OrderService orderService;

    public OrderController (OrderService orderService){
        this.orderService=orderService;

    }



    @PostMapping
    public ResponseEntity<?> createOrder (@AuthenticationPrincipal String username){
        orderService

                return;
    }
}
