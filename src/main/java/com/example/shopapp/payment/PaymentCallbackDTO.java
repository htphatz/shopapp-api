package com.example.shopapp.payment;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class PaymentCallbackDTO {
    @JsonProperty("code")
    private String code;

    @JsonProperty("message")
    private String message;
}
