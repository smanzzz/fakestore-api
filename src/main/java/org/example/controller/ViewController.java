package org.example.controller;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import org.example.dto.FakeStoreProductResponseDTO;
import org.example.dto.OrderRequestDTO;
import org.example.dto.UserRequestCreateDTO;
import org.example.dto.UserRequestLoginDTO;
import org.example.service.CartService;
import org.example.service.FakeStoreProductService;
import org.example.service.OrderService;
import org.example.service.UserService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseCookie;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.time.Duration;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Controller
public class ViewController {

    private final FakeStoreProductService productService;
    private final UserService userService;
    private final CartService cartService;
    private final OrderService orderService;

    @Value("${COOKIE_SECURE:false}")
    private boolean cookieSecure;

    public ViewController(
            FakeStoreProductService productService,
            UserService userService,
            CartService cartService,
            OrderService orderService) {
        this.productService = productService;
        this.userService = userService;
        this.cartService = cartService;
        this.orderService = orderService;
    }

    @GetMapping("/")
    public String home() {
        return "redirect:/products";
    }

    @GetMapping("/login")
    public String loginPage(Model model, HttpSession session) {
        model.addAttribute("cartCount", getCartCount(session));
        return "login";
    }

    @PostMapping("/login-form")
    public String processLogin(
            @RequestParam String username,
            @RequestParam String password,
            HttpServletRequest request,
            HttpServletResponse response,
            HttpSession session,
            Model model) {
        try {
            String token = userService.loginUser(new UserRequestLoginDTO(username, password));
            addJwtCookie(response, request, token, Duration.ofHours(24));
            return "redirect:/products";
        } catch (Exception e) {
            model.addAttribute("error", "Fel användarnamn eller lösenord.");
            model.addAttribute("cartCount", getCartCount(session));
            return "login";
        }
    }

    @GetMapping("/logout")
    public String processLogout(HttpServletRequest request, HttpServletResponse response, HttpSession session) {
        addJwtCookie(response, request, "", Duration.ZERO);
        session.invalidate();
        return "redirect:/products";
    }

    @GetMapping("/register")
    public String registerPage(Model model, HttpSession session) {
        model.addAttribute("cartCount", getCartCount(session));
        return "register";
    }

    @PostMapping("/register-form")
    public String processRegister(
            @RequestParam String username,
            @RequestParam String email,
            @RequestParam String password,
            HttpSession session,
            Model model) {
        try {
            userService.createUser(new UserRequestCreateDTO(username, email, password));
            return "redirect:/login?registered=true";
        } catch (Exception e) {
            model.addAttribute("error", e.getMessage());
            model.addAttribute("cartCount", getCartCount(session));
            return "register";
        }
    }

    @GetMapping("/products")
    public String productsPage(Model model, HttpSession session) {
        model.addAttribute("products", productService.getAllProducts());
        model.addAttribute("cartCount", getCartCount(session));
        return "products";
    }

    @GetMapping("/products/{id}")
    public String productDetail(@PathVariable Long id, Model model, HttpSession session) {
        model.addAttribute("product", productService.getProductById(id));
        model.addAttribute("cartCount", getCartCount(session));
        return "product-detail";
    }

    @GetMapping("/cart")
    public String cartPage(Model model, HttpSession session) {
        Map<Long, Integer> cart = cartService.getCart(session);
        List<CartItemView> cartItems = new ArrayList<>();
        double total = 0;

        for (Map.Entry<Long, Integer> entry : cart.entrySet()) {
            FakeStoreProductResponseDTO product = productService.getProductById(entry.getKey());
            if (product != null) {
                double itemTotal = product.price().doubleValue() * entry.getValue();
                total += itemTotal;
                cartItems.add(new CartItemView(product, entry.getValue(), itemTotal));
            }
        }

        model.addAttribute("cartItems", cartItems);
        model.addAttribute("total", total);
        model.addAttribute("cartCount", getCartCount(session));
        return "cart";
    }

    @PostMapping("/cart/add")
    public String addToCart(@RequestParam Long productId, HttpSession session) {
        cartService.addToCart(session, productId, 1);
        return "redirect:/products";
    }

    @PostMapping("/cart/remove")
    public String removeFromCart(@RequestParam Long productId, HttpSession session) {
        cartService.removeFromCart(session, productId);
        return "redirect:/cart";
    }

    @PostMapping("/checkout")
    public String checkout(HttpSession session, @AuthenticationPrincipal String username) {
        if (username == null || username.equals("anonymousUser")) {
            return "redirect:/login?checkout=true";
        }

        OrderRequestDTO orderRequest = cartService.createOrderRequestFromCart(session);
        if (orderRequest.orderItemList().isEmpty()) {
            return "redirect:/cart";
        }

        orderService.createOrder(username, orderRequest);
        cartService.clearCart(session);

        return "redirect:/order-confirmation";
    }

    @GetMapping("/order-confirmation")
    public String orderConfirmation(Model model, HttpSession session) {
        model.addAttribute("cartCount", getCartCount(session));
        return "order-confirmation";
    }

    private int getCartCount(HttpSession session) {
        return cartService.getCart(session).values().stream().mapToInt(Integer::intValue).sum();
    }

    private void addJwtCookie(HttpServletResponse response, HttpServletRequest request, String token, Duration maxAge) {
        ResponseCookie cookie = ResponseCookie.from("jwt", token)
                .httpOnly(true)
                .secure(cookieSecure || request.isSecure())
                .sameSite("Lax")
                .path("/")
                .maxAge(maxAge)
                .build();
        response.addHeader(HttpHeaders.SET_COOKIE, cookie.toString());
    }

    public static class CartItemView {
        public FakeStoreProductResponseDTO product;
        public int quantity;
        public double total;

        public CartItemView(FakeStoreProductResponseDTO product, int quantity, double total) {
            this.product = product;
            this.quantity = quantity;
            this.total = total;
        }
    }
}
