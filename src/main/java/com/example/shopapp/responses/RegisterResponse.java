package com.example.shopapp.responses;

import lombok.*;

@Getter
@Builder
public class RegisterResponse {
    // SUCCESS | EMAIL_DUPLICATED
    private String status;

    private String message;

    public RegisterResponse(String status, String message) {
        this.status = status;
        this.message = message;
    }
}
