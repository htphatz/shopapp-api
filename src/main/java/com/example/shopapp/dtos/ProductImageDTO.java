package com.example.shopapp.dtos;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.Size;
import lombok.*;


@Data // toString
@Builder
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class ProductImageDTO {
    @Min(value = 1, message = "Product's ID must be > 0")
    @JsonProperty("product_id")
    private Long productId;

    @Size(min = 5, max = 200, message = "Name must be between 3 and 200 characters")
    @JsonProperty("image_url")
    private String imageUrl;
}
