package com.example.shopapp.payment;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class PaymentDTO {
    @JsonProperty("payment_url")
    private String paymentUrl;
}
