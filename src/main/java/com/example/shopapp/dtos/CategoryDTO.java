package com.example.shopapp.dtos;

import jakarta.validation.constraints.NotEmpty;
import lombok.*;
import org.springframework.web.multipart.MultipartFile;

@Getter
@Builder
public class CategoryDTO {
    @NotEmpty(message = "Category's name cannot be empty")
    private String name;

    private MultipartFile fileImage;

    private String imageUrl;
}
