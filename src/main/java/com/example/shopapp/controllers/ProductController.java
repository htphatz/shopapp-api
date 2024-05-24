package com.example.shopapp.controllers;

import com.example.shopapp.dtos.ProductDTO;
import com.example.shopapp.models.Product;
import com.example.shopapp.responses.ArrayDataResponse;
import com.example.shopapp.responses.ResponseCustom;
import com.example.shopapp.services.ProductService;
import com.github.javafaker.Faker;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("${api.prefix}/products")
@RequiredArgsConstructor
public class ProductController {
    private final ProductService productService;

    @PostMapping(consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ArrayDataResponse<Product> createProduct(@Valid ProductDTO productDTO) {
        Product newProduct = productService.createProduct(productDTO);
        return new ArrayDataResponse<>(HttpStatus.OK.value(), "Create category successfully", List.of(newProduct));
    }

    @GetMapping
    public ResponseCustom<List<Product>> getProducts(
            @RequestParam(defaultValue = "") String keyword,
            @RequestParam(defaultValue = "0", name = "category_id") Long categoryId
    ) {
        List<Product> products = productService.getAllProducts(keyword, categoryId);
        return new ResponseCustom<>(HttpStatus.OK.value(), "Get all products successfully", products);
    }

    @GetMapping("/{id}")
    public ArrayDataResponse<Product> getProductById(@Valid @PathVariable("id") long id) {
        Product existingProduct = productService.getProductById(id);
        return new ArrayDataResponse<>(HttpStatus.OK.value(), "Get product with id " + id + " successfully", List.of(existingProduct));
    }

    @PutMapping("/{id}")
    public ResponseCustom<?> updateProducts(@Valid @PathVariable("id") Long id, @Valid ProductDTO productDTO) {
        productService.updateProduct(id, productDTO);
        return new ResponseCustom<>(HttpStatus.ACCEPTED.value(), "Update product with id " + id + " successfully");
    }

    @DeleteMapping("/{id}")
    public ResponseCustom<?> deleteProduct(@Valid @PathVariable("id") Long id) {
        productService.deleteProduct(id);
        return new ResponseCustom<>(HttpStatus.NO_CONTENT.value(), "Delete product with id " + id + " successfully");
    }

    @PostMapping("/generateFakeProducts")
    public ResponseEntity<String> generateFakeProducts() {
        Faker faker = new Faker();
        for (int i = 0; i < 2000; i++) {
            String productName = faker.commerce().productName();
            if (productService.existsByName(productName)) {
                continue;
            }
            ProductDTO productDTO = ProductDTO.builder()
                    .name(productName)
                    .price((double)faker.number().numberBetween(10, 90000000))
                    .description(faker.lorem().sentence())
                    .imageUrl("")
                    .categoryId((long)faker.number().numberBetween(5, 9))
                    .build();
            try {
                productService.createProduct(productDTO);
            } catch (Exception e) {
                return ResponseEntity.badRequest().body(e.getMessage());
            }
        }
        return ResponseEntity.ok("Fake products created successfully");
    }
}
