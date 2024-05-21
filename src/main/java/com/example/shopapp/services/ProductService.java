package com.example.shopapp.services;

import com.example.shopapp.responses.ProductResponse;
import com.example.shopapp.dtos.ProductDTO;
import com.example.shopapp.dtos.ProductImageDTO;
import com.example.shopapp.exceptions.DataNotFoundException;
import com.example.shopapp.models.Category;
import com.example.shopapp.models.Product;
import com.example.shopapp.models.ProductImage;
import com.example.shopapp.repositories.CategoryRepository;
import com.example.shopapp.repositories.ProductImageRepository;
import com.example.shopapp.repositories.ProductRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Map;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class ProductService implements IProductService {
    private final ProductRepository productRepository;
    private final CategoryRepository categoryRepository;
    private final ProductImageRepository productImageRepository;
    private final CloudinaryService cloudinaryService;

    @Value("${resource.noImageUrl}")
    private String noImageUrl;

    @Override
    public Product createProduct(ProductDTO productDTO) throws DataNotFoundException {
        Category existingCategory = categoryRepository
                .findById(productDTO.getCategoryId())
                .orElseThrow(() -> new DataNotFoundException("Category with id " + productDTO.getCategoryId() + " not found"));
        Product newProduct = Product.builder()
                .name(productDTO.getName())
                .price(productDTO.getPrice())
                .description(productDTO.getDescription())
                .category(existingCategory)
                .build();
        String imageUrl = noImageUrl;
        // Nếu tồn tại file thì upload lên Cloudinary
        if (productDTO.getFileImage() != null && !productDTO.getFileImage().isEmpty()) {
            Map data = this.cloudinaryService.upload(productDTO.getFileImage());
            imageUrl = (String) data.get("secure_url");
        }
        newProduct.setImageUrl(imageUrl);
        return productRepository.save(newProduct);
    }

    @Override
    public Product getProductById(long id) throws DataNotFoundException {
        return productRepository.findById(id)
                .orElseThrow(() -> new DataNotFoundException("Cannot find product with id " + id));
    }

    @Override
    public List<Product> getAllProducts(String keyword, Long categoryId) {
        return productRepository.searchProducts(categoryId, keyword);
    }

    @Override
    @Transactional
    public Product updateProduct(long id, ProductDTO productDTO) throws DataNotFoundException {
        Product existingProduct = getProductById(id);
        if (existingProduct != null) {
            // Copy cac thuoc tinh tu DTO sang product
            // Co the su dung ModelMapper
            Category existingCategory = categoryRepository
                    .findById(productDTO.getCategoryId())
                    .orElseThrow(() -> new DataNotFoundException("Category with id " + productDTO.getCategoryId() + " not found"));
            existingProduct.setName(productDTO.getName());
            existingProduct.setPrice(productDTO.getPrice());
            existingProduct.setDescription(productDTO.getDescription());
            existingProduct.setCategory(existingCategory);
            existingProduct.setImageUrl(existingProduct.getImageUrl());
            if (productDTO.getFileImage() != null && !productDTO.getFileImage().isEmpty()) {
                Map data = this.cloudinaryService.upload(productDTO.getFileImage());
                String newImageUrl = (String) data.get("secure_url");
                existingProduct.setImageUrl(newImageUrl);
            }
            return productRepository.save(existingProduct);
        }
        return null;
    }

    @Override
    @Transactional
    public void deleteProduct(long id) {
        Optional<Product> optionalProduct = productRepository.findById(id);
        if (optionalProduct.isPresent()) {
            productRepository.delete(optionalProduct.get());
        }
    }

    @Override
    public boolean existsByName(String name) {
        return productRepository.existsByName(name);
    }

    @Override
    public ProductImage createProductImage(long productId,
                                            ProductImageDTO productImageDTO) throws Exception {
        Product existingProduct = productRepository
                .findById(productId)
                .orElseThrow(() -> new DataNotFoundException("Product with id " + productImageDTO.getProductId() + " not found"));
        ProductImage newProductImage = ProductImage.builder()
                .product(existingProduct)
                .imageUrl(productImageDTO.getImageUrl())
                .build();
        return productImageRepository.save(newProductImage);
    }
}
