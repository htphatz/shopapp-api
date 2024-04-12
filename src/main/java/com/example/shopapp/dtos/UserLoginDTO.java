package com.example.shopapp.dtos;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.NotBlank;
import lombok.*;

@Data // toString
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class UserLoginDTO {
    @JsonProperty("phone")
    @NotBlank(message = "Phone number is required")
    private String phone;
    
    @NotBlank(message = "Password cannot be blank")
    private String password;

//    @JsonProperty("role_id")
//    @NotBlank(message = "Role id cannot be blank")
//    private Long roleId;
}
