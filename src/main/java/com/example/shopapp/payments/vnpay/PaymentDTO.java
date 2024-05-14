package com.example.shopapp.payments.vnpay;

import lombok.*;

@Data // toString
@Getter
@Setter
public abstract class PaymentDTO {
    @Builder
    public static class VNPayResponse {
        public String code;
        public String message;
        public String paymentUrl;
    }
}
