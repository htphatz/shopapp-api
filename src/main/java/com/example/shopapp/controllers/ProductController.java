package com.example.shopapp.controllers;

import com.example.shopapp.dtos.ProductDTO;
import com.example.shopapp.exceptions.DataNotFoundException;
import com.example.shopapp.models.Product;
import com.example.shopapp.responses.ResponseCustom;
import com.example.shopapp.services.ProductService;
import com.github.javafaker.Faker;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("${api.prefix}/products")
@RequiredArgsConstructor
public class ProductController {
    private final ProductService productService;

    @PostMapping(consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseCustom<?> createProduct(
            ProductDTO productDTO,
            BindingResult result)
    {
        try {
            if (result.hasErrors()) {
                List<String> errorMessages = result.getFieldErrors()
                        .stream()
                        .map(FieldError::getDefaultMessage)
                        .toList();
                return new ResponseCustom<>(HttpStatus.BAD_REQUEST.value(), "Create product failed");
            }
            Product newProduct = productService.createProduct(productDTO);
            List<Product> products = new ArrayList<Product>();
            products.add(newProduct);
            return new ResponseCustom<>(HttpStatus.CREATED.value(), "Create category successfully", products);
        } catch (Exception e) {
            return new ResponseCustom<>(HttpStatus.BAD_REQUEST.value(), e.getMessage());
        }
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
    public ResponseCustom<?> getProductById(@PathVariable("id") long id) throws DataNotFoundException {
        try {
            Product existingProduct = productService.getProductById(id);
            return new ResponseCustom<>(HttpStatus.OK.value(), "Get product with id " + id + " successfully", existingProduct);
        } catch (Exception e) {
            return new ResponseCustom<>(HttpStatus.BAD_REQUEST.value(), "Cannot get product with id " + id);
        }
    }

    @PutMapping("/{id}")
    public ResponseCustom<?> updateProducts(
            @PathVariable("id") Long id,
            ProductDTO productDTO) {
        try {
            productService.updateProduct(id, productDTO);
            return new ResponseCustom<>(HttpStatus.ACCEPTED.value(), "Update product with id " + id + " successfully");
        } catch (Exception e) {
            return new ResponseCustom<>(HttpStatus.BAD_REQUEST.value(), "Cannot update product with id " + id);
        }
    }

    @DeleteMapping("/{id}")
    public ResponseCustom<?> deleteProduct(@PathVariable("id") Long id) {
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
                    .price((float)faker.number().numberBetween(10, 90000000))
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
