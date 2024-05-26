package com.example.shopapp.responses;

import com.example.shopapp.entities.Order;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class OrderResponse {
    private Long id;

    @JsonProperty("full_name")
    private String fullName;

    private String email;

    private String phone;

    private String address;

    private String note;

    private String status;

    @JsonProperty("total_money")
    private Double totalMoney;

    @JsonProperty("payment_method")
    private String paymentMethod;

    public static OrderResponse fromOrder(Order order) {
        return OrderResponse.builder()
                .id(order.getId())
                .fullName(order.getFullName())
                .phone(order.getPhone())
                .email(order.getEmail())
                .address(order.getAddress())
                .note(order.getNote())
                .status(order.getStatus())
                .paymentMethod(order.getPaymentMethod())
                .totalMoney(order.getTotalMoney())
                .build();
    }
}
