package com.example.shopapp.dtos;

import com.example.shopapp.entities.OrderItem;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class CartItemDTO {
    @JsonProperty("product_id")
    private Long productId;

    @JsonProperty("price")
    private Double price;

    @JsonProperty("quantity")
    private int quantity;

    @JsonProperty("total_money")
    private Double totalMoney;

    public static CartItemDTO fromOrderItem(OrderItem orderItem) {
        return CartItemDTO.builder()
                .productId(orderItem.getProduct().getId())
                .price(orderItem.getPrice())
                .quantity(orderItem.getQuantity())
                .totalMoney(orderItem.getTotalMoney())
                .build();
    }
}
