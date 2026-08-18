package org.example.controller;

import org.example.dto.FakeStoreProductResponseDTO;
import org.example.service.CartService;
import org.example.service.FakeStoreProductService;
import org.example.service.OrderService;
import org.example.service.UserService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.servlet.view.InternalResourceViewResolver;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.model;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.redirectedUrl;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.view;

@ExtendWith(MockitoExtension.class)
class ViewControllerTest {

    private MockMvc mockMvc;

    @Mock
    private FakeStoreProductService productService;

    @Mock
    private UserService userService;

    @Mock
    private CartService cartService;

    @Mock
    private OrderService orderService;

    @InjectMocks
    private ViewController viewController;

    @BeforeEach
    void setUp() {
        InternalResourceViewResolver viewResolver = new InternalResourceViewResolver();
        viewResolver.setPrefix("/templates/");
        viewResolver.setSuffix(".html");

        mockMvc = MockMvcBuilders.standaloneSetup(viewController)
                .setViewResolvers(viewResolver)
                .build();
    }

    @Test
    void home_ShouldRedirectToProducts() throws Exception {
        mockMvc.perform(get("/"))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/products"));
    }

    @Test
    void loginPage_ShouldIncludeCartCount() throws Exception {
        when(cartService.getCart(any())).thenReturn(new HashMap<>());

        mockMvc.perform(get("/login"))
                .andExpect(status().isOk())
                .andExpect(view().name("login"))
                .andExpect(model().attributeExists("cartCount"));
    }

    @Test
    void registerPage_ShouldIncludeCartCount() throws Exception {
        when(cartService.getCart(any())).thenReturn(new HashMap<>());

        mockMvc.perform(get("/register"))
                .andExpect(status().isOk())
                .andExpect(view().name("register"))
                .andExpect(model().attributeExists("cartCount"));
    }

    @Test
    void favicon_ShouldReturnNoContent() throws Exception {
        mockMvc.perform(get("/favicon.ico"))
                .andExpect(status().isNoContent());
    }

    @Test
    void productsPage_ShouldReturnProductsView() throws Exception {
        FakeStoreProductResponseDTO product = new FakeStoreProductResponseDTO(
                1L,
                "Backpack",
                BigDecimal.valueOf(100.0),
                "Everyday backpack",
                "bags",
                "backpack.jpg");

        when(productService.getAllProducts()).thenReturn(List.of(product));
        when(cartService.getCart(any())).thenReturn(new HashMap<>());

        mockMvc.perform(get("/products"))
                .andExpect(status().isOk())
                .andExpect(view().name("products"))
                .andExpect(model().attributeExists("products"))
                .andExpect(model().attributeExists("cartCount"));
    }

    @Test
    void cartPage_ShouldReturnCartView() throws Exception {
        Map<Long, Integer> cart = new HashMap<>();
        cart.put(1L, 2);

        FakeStoreProductResponseDTO product = new FakeStoreProductResponseDTO(
                1L,
                "Backpack",
                BigDecimal.valueOf(100.0),
                "Everyday backpack",
                "bags",
                "backpack.jpg");

        when(cartService.getCart(any())).thenReturn(cart);
        when(productService.getProductById(1L)).thenReturn(product);

        mockMvc.perform(get("/cart"))
                .andExpect(status().isOk())
                .andExpect(view().name("cart"))
                .andExpect(model().attributeExists("cartItems"))
                .andExpect(model().attributeExists("total"));
    }
}
