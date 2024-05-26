package com.example.shopapp.dtos;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.NotBlank;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class ChangePasswordRequest {
    @NotBlank(message = "Password is required")
    private String password;

    @NotBlank(message = "New password is required")
    private String newPassword;
}
