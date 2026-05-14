package org.example.mapper;

import org.example.dto.FakeStoreProductResponseDTO;
import org.example.model.Product;
import org.springframework.stereotype.Component;

@Component
public class ProductMapper {

    public FakeStoreProductResponseDTO toDTO(Product product) {
        return new FakeStoreProductResponseDTO(
                product.getId(),
                product.getTitle(),
                product.getPrice(),
                product.getDescription(),
                product.getCategory(),
                product.getImage()
        );
    }


    public Product fromFakeStoreDTO(FakeStoreProductResponseDTO dto) {
        Product product = new Product();
        product.setTitle(dto.title());
        product.setPrice(dto.price());
        product.setDescription(dto.description());
        product.setCategory(dto.category());
        product.setImage(dto.image());
        return product;
    }
}
