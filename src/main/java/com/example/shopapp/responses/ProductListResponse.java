package com.example.shopapp.responses;

import lombok.*;

import java.util.List;

@Data // toString
@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ProductListResponse {
    private List<ProductResponse> products;

    private int totalPages;
}
