package com.example.shopapp.dtos;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.Min;
import lombok.*;

@Getter
@Builder
public class OrderDetailDTO {
    @Min(value = 1, message = "Order's ID must be >= 1")
    @JsonProperty("order_id")
    private Long orderId;

    @Min(value = 1, message = "Product's ID must be >= 1")
    @JsonProperty("product_id")
    private Long productId;

    @Min(value = 0, message = "Price must be >= 0")
    private Float price;

    @Min(value = 1, message = "Number of products must be >= 1")
    @JsonProperty("number_of_products")
    private int numberOfProducts;

    @Min(value = 0, message = "Total money must be >= 0")
    @JsonProperty("total_money")
    private Float totalMoney;

    private String color;
}
