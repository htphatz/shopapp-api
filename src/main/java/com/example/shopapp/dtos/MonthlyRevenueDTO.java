package com.example.shopapp.dtos;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;

@Getter
@Builder
@Data
@NoArgsConstructor
@AllArgsConstructor
public class MonthlyRevenueDTO {
    private int month;

    @JsonProperty("total_revenue")
    private double totalRevenue;
}
