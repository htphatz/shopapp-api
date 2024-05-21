package com.example.shopapp.responses;

import com.example.shopapp.models.User;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.*;

@Getter
@Builder
@AllArgsConstructor
public class RegisterResponse {
    // SUCCESS | EMAIL_DUPLICATED
    private String status;

    private String message;

    @JsonInclude(JsonInclude.Include.NON_NULL)
    private User user;

    public RegisterResponse(String status, String message) {
        this.status = status;
        this.message = message;
    }
}
