package org.example.service;

import org.example.dto.OrderItemDTO;
import org.example.dto.OrderRequestDTO;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.mock.web.MockHttpSession;

import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

class CartServiceTest {

    private CartService cartService;
    private MockHttpSession session;

    @BeforeEach
    void setUp() {
        cartService = new CartService();
        session = new MockHttpSession();
    }

    @Test
    void getCart_ShouldReturnEmptyMapWhenNoCartInSession() {
        Map<Long, Integer> cart = cartService.getCart(session);
        assertNotNull(cart);
        assertTrue(cart.isEmpty());
    }

    @Test
    void addToCart_ShouldAddNewProduct() {
        cartService.addToCart(session, 1L, 2);
        
        Map<Long, Integer> cart = cartService.getCart(session);
        assertEquals(1, cart.size());
        assertEquals(2, cart.get(1L));
    }

    @Test
    void addToCart_ShouldIncreaseQuantityIfProductExists() {
        cartService.addToCart(session, 1L, 2);
        cartService.addToCart(session, 1L, 3);
        
        Map<Long, Integer> cart = cartService.getCart(session);
        assertEquals(1, cart.size());
        assertEquals(5, cart.get(1L));
    }

    @Test
    void removeFromCart_ShouldRemoveProduct() {
        cartService.addToCart(session, 1L, 2);
        cartService.addToCart(session, 2L, 1);
        
        cartService.removeFromCart(session, 1L);
        
        Map<Long, Integer> cart = cartService.getCart(session);
        assertEquals(1, cart.size());
        assertNull(cart.get(1L));
        assertEquals(1, cart.get(2L));
    }

    @Test
    void clearCart_ShouldRemoveCartFromSession() {
        cartService.addToCart(session, 1L, 2);
        cartService.clearCart(session);
        
        Map<Long, Integer> cart = cartService.getCart(session);
        assertTrue(cart.isEmpty());
    }

    @Test
    void createOrderRequestFromCart_ShouldReturnCorrectOrderRequest() {
        cartService.addToCart(session, 100L, 2);
        cartService.addToCart(session, 101L, 1);
        
        OrderRequestDTO requestDTO = cartService.createOrderRequestFromCart(session);
        
        assertNotNull(requestDTO);
        assertEquals(2, requestDTO.orderItemList().size());
        
        boolean found100 = false;
        boolean found101 = false;
        
        for (OrderItemDTO item : requestDTO.orderItemList()) {
            if (item.productId().equals(100L)) {
                assertEquals(2, item.amountOfProduct());
                found100 = true;
            } else if (item.productId().equals(101L)) {
                assertEquals(1, item.amountOfProduct());
                found101 = true;
            }
        }
        
        assertTrue(found100);
        assertTrue(found101);
    }
}
