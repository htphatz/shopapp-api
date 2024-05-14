package com.example.shopapp.dtos;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.NotBlank;
import lombok.*;

@Data
@Builder
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class VoucherDTO {
    @NotBlank(message = "Voucher code is required")
    private String code;

    @NotBlank(message = "Discount type cannot be blank")
    @JsonProperty("discount_type")
    private String discountType;

    @NotBlank(message = "Discount value cannot be blank")
    @JsonProperty("discount_value")
    private Double discountValue;
}
