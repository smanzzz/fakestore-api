package org.example.service;

import jakarta.servlet.http.HttpSession;
import org.example.dto.OrderItemDTO;
import org.example.dto.OrderRequestDTO;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class CartService {

    private static final String CART_SESSION_KEY = "shopping_cart";

    @SuppressWarnings("unchecked")
    public Map<Long, Integer> getCart(HttpSession session) {
        Map<Long, Integer> cart = (Map<Long, Integer>) session.getAttribute(CART_SESSION_KEY);
        if (cart == null) {
            cart = new HashMap<>();
            session.setAttribute(CART_SESSION_KEY, cart);
        }
        return cart;
    }

    public void addToCart(HttpSession session, Long productId, int quantity) {
        Map<Long, Integer> cart = getCart(session);
        cart.put(productId, cart.getOrDefault(productId, 0) + quantity);
    }

    public void removeFromCart(HttpSession session, Long productId) {
        Map<Long, Integer> cart = getCart(session);
        cart.remove(productId);
    }

    public void clearCart(HttpSession session) {
        session.removeAttribute(CART_SESSION_KEY);
    }

    public OrderRequestDTO createOrderRequestFromCart(HttpSession session) {
        Map<Long, Integer> cart = getCart(session);
        List<OrderItemDTO> orderItems = new ArrayList<>();
        
        for (Map.Entry<Long, Integer> entry : cart.entrySet()) {
            orderItems.add(new OrderItemDTO(entry.getKey(), entry.getValue()));
        }
        
        return new OrderRequestDTO(orderItems);
    }
}
