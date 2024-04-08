package com.example.shopapp.responses;

import com.example.shopapp.models.User;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;

@Data // toString
@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class RegisterResponse {
    @JsonProperty("message")
    private String message;

    @JsonProperty("user")
    private User user;
}