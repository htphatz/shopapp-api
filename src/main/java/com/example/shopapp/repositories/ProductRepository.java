package com.example.shopapp.repositories;

import com.example.shopapp.models.Product;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ProductRepository extends JpaRepository<Product, Long> {
    boolean existsByName(String name);

    Page<Product> findAll(Pageable pageable); // Phan trang

    @Query("SELECT p FROM Product p WHERE " +
        "(:categoryId IS NULL OR :categoryId = 0 OR p.category.id = :categoryId) " +
        "AND (:keyword IS NULL OR :keyword = '' OR p.name LIKE :keyword% OR p.description LIKE :keyword%)")
    List<Product> searchProducts(@Param("categoryId") Long categoryId,
                                 @Param("keyword") String keyword);
}
