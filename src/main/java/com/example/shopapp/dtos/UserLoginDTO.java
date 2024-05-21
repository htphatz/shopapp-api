package com.example.shopapp.dtos;

import jakarta.validation.constraints.NotBlank;
import lombok.*;

@Getter
@Builder
public class UserLoginDTO {
    @NotBlank(message = "Email number is required")
    private String email;
    
    @NotBlank(message = "Password cannot be blank")
    private String password;
}
