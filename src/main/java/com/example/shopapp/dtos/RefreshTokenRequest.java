package com.example.shopapp.dtos;

import lombok.*;

@Getter
@Builder
public class RefreshTokenRequest {
    private String token;
}
