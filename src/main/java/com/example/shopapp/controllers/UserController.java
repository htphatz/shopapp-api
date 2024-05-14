package com.example.shopapp.controllers;

import com.example.shopapp.components.JwtTokenUtils;
import com.example.shopapp.dtos.RefreshTokenRequest;
import com.example.shopapp.dtos.UserDTO;
import com.example.shopapp.dtos.UserLoginDTO;
import com.example.shopapp.exceptions.RefreshTokenExpiredException;
import com.example.shopapp.exceptions.RefreshTokenNotFoundException;
import com.example.shopapp.models.RefreshToken;
import com.example.shopapp.models.User;
import com.example.shopapp.responses.JwtResponse;
import com.example.shopapp.responses.LoginResponse;
import com.example.shopapp.responses.RegisterResponse;
import com.example.shopapp.services.RefreshTokenService;
import com.example.shopapp.services.UserService;
import com.example.shopapp.components.LocalizationUtils;
import com.example.shopapp.utils.MessageKeys;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("${api.prefix}/users")
@RequiredArgsConstructor
public class UserController {
    private final UserService userService;
    private final LocalizationUtils localizationUtils;
    private final JwtTokenUtils jwtTokenUtils;
    private final RefreshTokenService refreshTokenService;

    @PostMapping("/register")
    public ResponseEntity<RegisterResponse> createUser(
            @Valid @RequestBody UserDTO userDTO,
            BindingResult result) {
        try {
            if (result.hasErrors()) {
                List<String> errorMessages = result.getFieldErrors()
                        .stream()
                        .map(FieldError::getDefaultMessage)
                        .toList();
                return ResponseEntity.badRequest().body(
                        RegisterResponse.builder()
                                .message(localizationUtils.getLocalizedMessage(MessageKeys.REGISTER_FAILED, errorMessages))
                                .build()
                );
            }
            if(!userDTO.getPassword().equals(userDTO.getRetypePassword())) {
                return ResponseEntity.badRequest().body(
                        RegisterResponse.builder()
                                .message(localizationUtils.getLocalizedMessage(MessageKeys.PASSWORD_NOT_MATCH))
                                .build()
                );
            }
            User user = userService.createUser(userDTO);
            // return ResponseEntity.ok("Register successfully");
            return ResponseEntity.ok(RegisterResponse.builder()
                            .message(localizationUtils.getLocalizedMessage(MessageKeys.REGISTER_SUCCESSFULLY))
                            .user(user)
                    .build());
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(
                    RegisterResponse.builder()
                            .message(localizationUtils.getLocalizedMessage(MessageKeys.REGISTER_FAILED, e.getMessage()))
                            .build()
            );
        }
    }

    @PostMapping("/login")
    public ResponseEntity<LoginResponse> login(
            @Valid @RequestBody UserLoginDTO userLoginDTO) throws Exception {
        // Kiem tra thong tin dang nhap
        // Tra ve thong tin dang nhap va sinh token
        try {
            String accessToken = userService.login(
                    userLoginDTO.getPhone(),
                    userLoginDTO.getPassword()
            );
            RefreshToken refreshToken = refreshTokenService.createRefreshToken(userLoginDTO.getPhone());
            // Tra ve access-token va refresh-token trong response
            return ResponseEntity.ok(LoginResponse.builder()
                            .message(localizationUtils.getLocalizedMessage(MessageKeys.LOGIN_SUCCESSFULLY))
                            .accessToken(accessToken)
                            .refreshToken(refreshToken.getToken())
                    .build());
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(LoginResponse.builder()
                        .message(localizationUtils.getLocalizedMessage(MessageKeys.LOGIN_FAILED, e.getMessage()))
                    .build()
            );
        }
    }

    @PostMapping("/refreshToken")
    public ResponseEntity<?> refreshToken(@Valid @RequestBody RefreshTokenRequest
                                                      refreshTokenRequest) throws Exception {
        try {
            RefreshToken refreshToken = refreshTokenService.findByToken(refreshTokenRequest.getToken())
                    .orElseThrow(() -> new RefreshTokenNotFoundException("Cannot find refresh token "+ refreshTokenRequest.getToken()));
            refreshToken = refreshTokenService.verifyExpiration(refreshToken);
            if (refreshToken.isRevoked() || refreshToken.isExpired()) {
                throw new RefreshTokenExpiredException("Refresh token " + refreshToken.getToken()
                        + " was expired. Please make a sign in request");
            }
            User user = refreshToken.getUser();
            String accessToken = jwtTokenUtils.generateToken(user);
            return ResponseEntity.ok(JwtResponse.builder()
                    .accessToken(accessToken)
                    .refreshToken(refreshToken.getToken())
                    .build());
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }
}
