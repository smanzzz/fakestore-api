package org.example.repository;
import org.example.model.Product;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ProductsRepository extends JpaRepository<Product, Long> {
    boolean existsByTitle(String title);
}
