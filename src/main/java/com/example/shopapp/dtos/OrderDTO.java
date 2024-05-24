package com.example.shopapp.dtos;

import com.example.shopapp.models.Order;
import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.*;

import java.util.LinkedList;
import java.util.List;

@NoArgsConstructor
@AllArgsConstructor
@Data
@Builder
public class OrderDTO {
    private Long id;

    @Min(value = 1, message = "User's ID must be > 0")
    @JsonProperty("user_id")
    private Long userId;

    @JsonProperty("full_name")
    private String fullName;

    private String email;

    @NotBlank(message = "Phone number cannot be blank")
    @Size(min = 9, message = "Phone number must be at least 9 characters")
    @JsonProperty("phone")
    private String phone;

    private String address;

    private String note;

    private String status;

    @Min(value = 0, message = "Total money must be >= 0")
    @JsonProperty("total_money")
    private Double totalMoney;

    @JsonProperty("payment_method")
    private String paymentMethod;

    @JsonProperty("cart_items")
    private List<CartItemDTO> cartItems = new LinkedList<>();

    public static OrderDTO fromOrder(Order order) {
        return OrderDTO.builder()
                .id(order.getId())
                .userId(order.getUser().getId())
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
