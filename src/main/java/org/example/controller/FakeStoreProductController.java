package org.example.controller;

import org.example.dto.FakeStoreProductResponseDTO;
import org.example.service.FakeStoreProductService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/products")
public class FakeStoreProductController {

    private final FakeStoreProductService fakeStoreProductService;

    public FakeStoreProductController(FakeStoreProductService fakeStoreProductService) {
        this.fakeStoreProductService = fakeStoreProductService;
    }

    @GetMapping
    public ResponseEntity<List<FakeStoreProductResponseDTO>> getAllProducts() {
        return ResponseEntity.ok(fakeStoreProductService.getAllProducts());
    }

    @GetMapping("/{id}")
    public ResponseEntity<FakeStoreProductResponseDTO> getProductById(@PathVariable Long id) {
        return ResponseEntity.ok(fakeStoreProductService.getProductById(id));
    }

    @PostMapping("/sync")
    public ResponseEntity<String> syncProducts() {
        fakeStoreProductService.syncProducts();
        return ResponseEntity.ok("Produkter synkade från FakeStore API.");
    }
}
