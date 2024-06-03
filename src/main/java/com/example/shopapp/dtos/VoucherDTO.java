package com.example.shopapp.dtos;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Builder;
import lombok.Getter;

import java.util.Date;

@Getter
@Builder
public class VoucherDTO {
    @NotBlank(message = "Voucher code is required")
    private String code;

    @NotBlank(message = "Discount type cannot be blank")
    @JsonProperty("discount_type")
    private String discountType;

    @NotNull(message = "Discount value cannot be blank")
    @JsonProperty("discount_value")
    private Double discountValue;

    @NotNull(message = "Term cannot be blank")
    private Double term;

    @NotNull(message = "Expiration date cannot be blank")
    @JsonProperty("expiration_date")
    @JsonFormat(pattern = "yyyy-MM-dd")
    private Date expirationDate;
}

