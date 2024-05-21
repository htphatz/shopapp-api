package com.example.shopapp.responses;


import com.example.shopapp.models.Order;
import lombok.*;

import java.util.List;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class OrderListResponse {
    private List<Order> orders;
}
