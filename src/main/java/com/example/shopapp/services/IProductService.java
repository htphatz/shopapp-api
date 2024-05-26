package com.example.shopapp.services;

import com.example.shopapp.dtos.ProductDTO;
import com.example.shopapp.entities.Product;

import java.util.List;

public interface IProductService {
    Product createProduct(ProductDTO productDTO);
    Product getProductById(long id);
    List<Product> getAllProducts(String keyword, Long categoryId);
    Product updateProduct(long id, ProductDTO productDTO);
    void deleteProduct(long id);
    boolean existsByName(String name);
}
