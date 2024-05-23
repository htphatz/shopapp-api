package com.example.shopapp.responses;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;

@Data
@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class JwtResponse {
    @JsonProperty("access_token")
    private String accessToken;

    @JsonProperty("refresh_token")
    private String refreshToken;
}
