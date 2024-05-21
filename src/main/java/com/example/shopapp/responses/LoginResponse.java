package com.example.shopapp.responses;

import com.example.shopapp.models.User;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;

@Data // toString
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class LoginResponse {
    @JsonProperty("message")
    private String message;

    @JsonProperty("access_token")
    private String accessToken;

    @JsonProperty("refresh_token")
    private String refreshToken;

    private User user;
}
