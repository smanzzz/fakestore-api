package org.example.service;

import org.example.client.FakeStoreProductClient;
import org.example.dto.FakeStoreProductResponseDTO;
import org.example.mapper.ProductMapper;
import org.example.model.Product;
import org.example.repository.ProductsRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class FakeStoreProductServiceTest {

    @Mock
    private FakeStoreProductClient fakeStoreProductClient;

    @Mock
    private ProductsRepository productsRepository;

    @Mock
    private ProductMapper productMapper;

    @InjectMocks
    private FakeStoreProductService productService;

    private Product product;
    private FakeStoreProductResponseDTO fakeStoreProductResponseDTO;

    @BeforeEach
    void setUp() {
        product = new Product();
        product.setId(1L);
        product.setTitle("Test Product");
        product.setPrice(BigDecimal.valueOf(100.0));
        product.setDescription("Description");
        product.setCategory("electronics");
        product.setImage("image.jpg");

        fakeStoreProductResponseDTO = new FakeStoreProductResponseDTO(
                1L,
                "Test Product",
                BigDecimal.valueOf(100.0),
                "Description",
                "electronics",
                "image.jpg"
        );
    }

    @Test
    void syncProducts_ShouldFetchAndSaveProducts() {
        when(fakeStoreProductClient.getAllProducts()).thenReturn(List.of(fakeStoreProductResponseDTO));
        when(productMapper.fromFakeStoreDTO(any())).thenReturn(product);

        productService.syncProducts();

        verify(productsRepository, times(1)).saveAll(anyList());
    }

    @Test
    void syncProducts_ShouldSkipProductsThatAlreadyExist() {
        when(fakeStoreProductClient.getAllProducts()).thenReturn(List.of(fakeStoreProductResponseDTO));
        when(productsRepository.existsByTitle(fakeStoreProductResponseDTO.title())).thenReturn(true);

        productService.syncProducts();

        verify(productsRepository, never()).saveAll(anyList());
    }

    @Test
    void syncProducts_ShouldNotSaveWhenApiReturnsNoProducts() {
        when(fakeStoreProductClient.getAllProducts()).thenReturn(List.of());

        productService.syncProducts();

        verify(productsRepository, never()).saveAll(anyList());
    }

    @Test
    void getAllProducts_ShouldReturnProductsFromDatabaseIfNotEmpty() {
        when(productsRepository.findAll()).thenReturn(List.of(product));
        when(productMapper.toDTO(any())).thenReturn(fakeStoreProductResponseDTO);

        List<FakeStoreProductResponseDTO> result = productService.getAllProducts();

        assertFalse(result.isEmpty());
        assertEquals(1, result.size());
        
        verify(fakeStoreProductClient, never()).getAllProducts();
    }

    @Test
    void getAllProducts_ShouldSyncIfDatabaseIsEmpty() {
        when(productsRepository.findAll()).thenReturn(List.of(), List.of(product));
        when(fakeStoreProductClient.getAllProducts()).thenReturn(List.of(fakeStoreProductResponseDTO));
        when(productMapper.fromFakeStoreDTO(any())).thenReturn(product);
        when(productMapper.toDTO(any())).thenReturn(fakeStoreProductResponseDTO);

        List<FakeStoreProductResponseDTO> result = productService.getAllProducts();

        assertFalse(result.isEmpty());
        verify(fakeStoreProductClient, times(1)).getAllProducts();
        verify(productsRepository, times(1)).saveAll(anyList());
    }

    @Test
    void getProductById_ShouldReturnProductFromDatabaseIfFound() {
        when(productsRepository.findById(1L)).thenReturn(Optional.of(product));
        when(productMapper.toDTO(any())).thenReturn(fakeStoreProductResponseDTO);

        FakeStoreProductResponseDTO result = productService.getProductById(1L);

        assertNotNull(result);
        assertEquals(1L, result.id());
        verify(fakeStoreProductClient, never()).getProductById(anyLong());
    }

    @Test
    void getProductById_ShouldReturnProductFromClientIfNotFoundInDatabase() {
        when(productsRepository.findById(1L)).thenReturn(Optional.empty());
        when(fakeStoreProductClient.getProductById(1L)).thenReturn(fakeStoreProductResponseDTO);

        FakeStoreProductResponseDTO result = productService.getProductById(1L);

        assertNotNull(result);
        assertEquals(1L, result.id());
        verify(fakeStoreProductClient, times(1)).getProductById(1L);
    }
}
