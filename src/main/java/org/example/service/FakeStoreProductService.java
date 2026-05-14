package org.example.service;

import org.example.client.FakeStoreProductClient;
import org.example.dto.FakeStoreProductResponseDTO;
import org.example.mapper.ProductMapper;
import org.example.model.Product;
import org.example.repository.ProductsRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class FakeStoreProductService {

    private final FakeStoreProductClient fakeStoreProductClient;
    private final ProductsRepository productsRepository;
    private final ProductMapper productMapper;

    public FakeStoreProductService(FakeStoreProductClient fakeStoreProductClient,
                                   ProductsRepository productsRepository,
                                   ProductMapper productMapper) {
        this.fakeStoreProductClient = fakeStoreProductClient;
        this.productsRepository = productsRepository;
        this.productMapper = productMapper;
    }

    public List<FakeStoreProductResponseDTO> getAllProducts() {
        List<Product> localProducts = productsRepository.findAll();

        if (localProducts.isEmpty()) {
            syncProducts();
            localProducts = productsRepository.findAll();
        }

        return localProducts.stream()
                .map(productMapper::toDTO)
                .toList();
    }

    public FakeStoreProductResponseDTO getProductById(Long id) {
        return productsRepository.findById(id)
                .map(productMapper::toDTO)
                .orElseGet(() -> fakeStoreProductClient.getProductById(id));
    }

    public void syncProducts() {
        List<FakeStoreProductResponseDTO> apiProducts = fakeStoreProductClient.getAllProducts();

        List<Product> products = apiProducts.stream()
                .map(productMapper::fromFakeStoreDTO)
                .toList();

        productsRepository.saveAll(products);
    }
}

