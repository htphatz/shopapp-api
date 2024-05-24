package com.example.shopapp.controllers;

import com.example.shopapp.components.JwtTokenUtils;
import com.example.shopapp.dtos.RefreshTokenRequest;
import com.example.shopapp.dtos.UserDTO;
import com.example.shopapp.dtos.UserLoginDTO;
import com.example.shopapp.exceptions.RefreshTokenExpiredException;
import com.example.shopapp.exceptions.ResourceNotFoundException;
import com.example.shopapp.models.RefreshToken;
import com.example.shopapp.models.Role;
import com.example.shopapp.models.User;
import com.example.shopapp.repositories.UserRepository;
import com.example.shopapp.responses.RefreshTokenResponse;
import com.example.shopapp.responses.LoginResponse;
import com.example.shopapp.responses.RegisterResponse;
import com.example.shopapp.services.RefreshTokenService;
import com.example.shopapp.services.UserService;
import com.example.shopapp.components.LocalizationUtils;
import com.example.shopapp.utils.MessageKeys;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;


import static com.example.shopapp.constants.UserConstant.*;

@RestController
@RequestMapping("${api.prefix}/users")
@RequiredArgsConstructor
public class UserController {
    private final UserService userService;
    private final UserRepository userRepository;
    private final LocalizationUtils localizationUtils;
    private final JwtTokenUtils jwtTokenUtils;
    private final RefreshTokenService refreshTokenService;

    @PostMapping("/register")
    public ResponseEntity<RegisterResponse> createUser(@Valid UserDTO userDTO) {
        User user = new User();
        user.setRole(new Role(1L, Role.USER));
        user = userService.createUser(userDTO);
        return  ResponseEntity.ok(new RegisterResponse(SUCCESS, USER_CREATED_SUCCESS));
    }

    @PostMapping("/login")
    public ResponseEntity<?> login(@Valid UserLoginDTO userLoginDTO) {
            String accessToken = userService.login(
                    userLoginDTO.getEmail(),
                    userLoginDTO.getPassword()
            );
            RefreshToken refreshToken = refreshTokenService.createRefreshToken(userLoginDTO.getEmail());
            // Tra ve message, access-token, refresh-token va thong tin user trong response
            return ResponseEntity.ok(LoginResponse.builder()
                            .message(localizationUtils.getLocalizedMessage(MessageKeys.LOGIN_SUCCESSFULLY))
                            .accessToken(accessToken)
                            .refreshToken(refreshToken.getToken())
                    .build());
    }

    @PostMapping("/refreshToken")
    public ResponseEntity<?> refreshToken(@Valid RefreshTokenRequest refreshTokenRequest) {
            RefreshToken refreshToken = refreshTokenService.findByToken(refreshTokenRequest.getToken())
                    .orElseThrow(() -> new ResourceNotFoundException("Cannot find refresh token "+ refreshTokenRequest.getToken()));
            refreshToken = refreshTokenService.verifyExpiration(refreshToken);
            if (refreshToken.isRevoked() || refreshToken.isExpired()) {
                throw new RefreshTokenExpiredException("Refresh token " + refreshToken.getToken()
                        + " was expired. Please make a sign in request");
            }
            User user = refreshToken.getUser();
            String accessToken = jwtTokenUtils.generateToken(user);
            return ResponseEntity.ok(RefreshTokenResponse.builder()
                    .accessToken(accessToken)
                    .refreshToken(refreshToken.getToken())
                    .build());
    }

    @GetMapping("/me")
    public ResponseEntity<?> getInformation() throws Exception {
        UsernamePasswordAuthenticationToken auth = (UsernamePasswordAuthenticationToken) SecurityContextHolder.getContext().getAuthentication();
        return ResponseEntity.ok(auth.getPrincipal());
    }

}
