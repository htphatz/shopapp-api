package com.example.shopapp.responses;

import com.example.shopapp.models.OrderItem;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class OrderDetailResponse {
    private Long id;

    @JsonProperty("order_id")
    private Long orderId;

    @JsonProperty("product_id")
    private Long productId;

    private Double price;

    @JsonProperty("number_of_products")
    private int numberOfProducts;

    @JsonProperty("total_money")
    private Double totalMoney;

    public static OrderDetailResponse fromOrderDetail(OrderItem orderItem) {
        return OrderDetailResponse.builder()
                .id(orderItem.getId())
                .orderId(orderItem.getOrder().getId())
                .productId(orderItem.getProduct().getId())
                .price(orderItem.getPrice())
                .numberOfProducts(orderItem.getQuantity())
                .totalMoney(orderItem.getTotalMoney())
                .build();
    }
}
