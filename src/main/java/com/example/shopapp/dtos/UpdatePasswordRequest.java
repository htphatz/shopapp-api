package com.example.shopapp.dtos;

import jakarta.validation.constraints.NotBlank;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class UpdatePasswordRequest {
    @NotBlank(message = "New password is required")
    private String newPassword;
}
