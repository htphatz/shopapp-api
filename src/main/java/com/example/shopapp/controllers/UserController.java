package com.example.shopapp.controllers;

import com.example.shopapp.components.JwtTokenUtils;
import com.example.shopapp.dtos.*;
import com.example.shopapp.exceptions.RefreshTokenExpiredException;
import com.example.shopapp.exceptions.ResourceNotFoundException;
import com.example.shopapp.entities.RefreshToken;
import com.example.shopapp.entities.Role;
import com.example.shopapp.entities.User;
import com.example.shopapp.repositories.UserRepository;
import com.example.shopapp.responses.RefreshTokenResponse;
import com.example.shopapp.responses.LoginResponse;
import com.example.shopapp.responses.RegisterResponse;
import com.example.shopapp.responses.ResponseCustom;
import com.example.shopapp.services.RefreshTokenService;
import com.example.shopapp.services.UserService;
import com.example.shopapp.components.LocalizationUtils;
import com.example.shopapp.utils.MessageKeys;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;


import java.util.List;

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

    @PostMapping("/refresh-token")
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
    public ResponseEntity<?> getInformation() {
        UsernamePasswordAuthenticationToken auth = (UsernamePasswordAuthenticationToken) SecurityContextHolder.getContext().getAuthentication();
        return ResponseEntity.ok(auth.getPrincipal());
    }

    @PostMapping("/change-password")
    public ResponseEntity<ResponseCustom<?>> changePassword(@Valid ChangePasswordRequest changePasswordRequest) {
        userService.changePassword(changePasswordRequest.getPassword(), changePasswordRequest.getNewPassword());
        return ResponseEntity.ok(new ResponseCustom<>(HttpStatus.OK.value(), "Change password successfully"));
    }

    @PostMapping("/forgot/{email}")
    public ResponseEntity<ResponseCustom<?>> verifyEmail(@PathVariable("email") String email) {
        userService.verifyEmail(email);
        return ResponseEntity.ok(new ResponseCustom<>(HttpStatus.OK.value(), "Please check email to have OTP code"));
    }

    @PostMapping("/update-password/{email}")
    public ResponseEntity<ResponseCustom<?>> updatePassword(@Valid UpdatePasswordRequest updatePasswordRequest, @Valid @PathVariable("email") String email) {
        userService.updatePassword(email, updatePasswordRequest.getNewPassword());
        return ResponseEntity.ok(new ResponseCustom<>(HttpStatus.OK.value(), "Password has been changed"));
    }

    @PostMapping("/verifyOtp/{otp}/{email}")
    public ResponseEntity<ResponseCustom<?>> verifyOtp(@Valid @PathVariable int otp, @Valid @PathVariable String email) {
        userService.verifyOtp(otp, email);
        return ResponseEntity.ok(new ResponseCustom<>(HttpStatus.OK.value(), "OTP verified!"));
    }

    @PatchMapping("/disable/{id}")
    public ResponseEntity<ResponseCustom<?>> disableUser(@Valid @PathVariable Long id) {
        userService.disableUser(id);
        return ResponseEntity.ok(new ResponseCustom<>(HttpStatus.ACCEPTED.value(), "Disable user with id" + id + " successfully"));
    }

    @PatchMapping("/enable/{id}")
    public ResponseEntity<ResponseCustom<?>> enable(@Valid @PathVariable Long id) {
        userService.enableUser(id);
        return ResponseEntity.ok(new ResponseCustom<>(HttpStatus.ACCEPTED.value(), "Enable user with id" + id + " successfully"));
    }

    @GetMapping("/get-all-user")
    public ResponseEntity<ResponseCustom<?>> getAllUser() {
        List<User> users = userService.getAllUsers();
        return ResponseEntity.ok(new ResponseCustom<>(HttpStatus.OK.value(), "Get all user successfully", users));
    }

}
