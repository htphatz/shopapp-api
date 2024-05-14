package com.example.shopapp.controllers;

import com.example.shopapp.dtos.ProductDTO;
import com.example.shopapp.exceptions.DataNotFoundException;
import com.example.shopapp.models.Product;
import com.example.shopapp.responses.ProductListResponse;
import com.example.shopapp.responses.ProductResponse;
import com.example.shopapp.services.ProductService;
import com.github.javafaker.Faker;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("${api.prefix}/products")
@RequiredArgsConstructor
public class ProductController {
    private final ProductService productService;

    @PostMapping(consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<?> createProduct(
            ProductDTO productDTO,
            BindingResult result)
    {
        try {
            if (result.hasErrors()) {
                List<String> errorMessages = result.getFieldErrors()
                        .stream()
                        .map(FieldError::getDefaultMessage)
                        .toList();
                return ResponseEntity.badRequest().body(errorMessages);
            }
            Product newProduct = productService.createProduct(productDTO);
            return ResponseEntity.ok(newProduct);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

//    @PostMapping(value = "uploads/{id}", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
//    public ResponseEntity<?> uploadImages(
//            @PathVariable("id") long productId,
//            @ModelAttribute("file") MultipartFile file) {
//        try {
//            Product existingProduct = productService.getProductById(productId);
//                if (file.getSize() == 0)
//                    return ResponseEntity.status(HttpStatus.NOT_FOUND)
//                            .body("File not found");
//                if (file.getSize() > 10 * 1024 * 1024) { // Kich thuoc hon 10MB
//                    return ResponseEntity.status(HttpStatus.PAYLOAD_TOO_LARGE)
//                            .body("File is too large, Maximum size is 10MB");
//                }
//                String contentType = file.getContentType();
//                if (contentType == null || !contentType.startsWith("image/")) {
//                    return ResponseEntity.status(HttpStatus.UNSUPPORTED_MEDIA_TYPE)
//                            .body("File must be an image");
//                }
//                // Luu file va cap nhat thumbnail
//                String filename = storeFile(file);
//                // Luu vao bang products
//                ProductImage productImage =  productService.createProductImage(existingProduct.getId(),
//                        ProductImageDTO.builder()
//                        .imageUrl(filename)
//                        .build()
//                );
//            return ResponseEntity.ok().body(productImage);
//        } catch (Exception e) {
//            return ResponseEntity.badRequest().body(e.getMessage());
//        }
//    }

    @GetMapping("")
    public ResponseEntity<ProductListResponse> getProducts(
            @RequestParam(defaultValue = "") String keyword,
            @RequestParam(defaultValue = "0", name = "category_id") Long categoryId,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "12") int limit
    ) {
        // Tao Pageable tu thong tin trang va gioi han
        PageRequest pageRequest = PageRequest.of(
                page, limit,
                Sort.by(("id")).ascending()
                // Sort.by("createdAt").descending()
        );
        Page<ProductResponse> productPage = productService.getAllProducts(keyword, categoryId, pageRequest);

        // Lay tong so trang
        int totalPages = productPage.getTotalPages();

        List<ProductResponse> products = productPage.getContent();
        return ResponseEntity.ok(ProductListResponse.builder()
                .products(products)
                .totalPages(totalPages)
                .build());
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> getProductById(@PathVariable("id") long productId) throws DataNotFoundException {
        try {
            Product existingProduct = productService.getProductById(productId);
            return ResponseEntity.ok(ProductResponse.fromProduct(existingProduct));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> updateProducts(
            @PathVariable("id") Long id,
            ProductDTO productDTO) {
        try {
            Product updatedProduct = productService.updateProduct(id, productDTO);
            return ResponseEntity.ok(updatedProduct);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteProduct(@PathVariable("id") Long id) {
        try {
            productService.deleteProduct(id);
            return ResponseEntity.ok("Product with id " + id + " deleted successfully");
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
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
