package com.example.shopapp.payment;

import lombok.*;

@Data
@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class VNPayResponse {
    private String code;
    private String message;
    private String paymentUrl;
}
