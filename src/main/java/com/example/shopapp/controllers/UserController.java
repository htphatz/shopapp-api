package com.example.shopapp.controllers;

import com.example.shopapp.components.JwtTokenUtils;
import com.example.shopapp.dtos.*;
import com.example.shopapp.entities.OTPReset;
import com.example.shopapp.exceptions.RefreshTokenExpiredException;
import com.example.shopapp.exceptions.ResourceNotFoundException;
import com.example.shopapp.entities.RefreshToken;
import com.example.shopapp.entities.Role;
import com.example.shopapp.entities.User;
import com.example.shopapp.repositories.OTPResetRepository;
import com.example.shopapp.repositories.UserRepository;
import com.example.shopapp.responses.RefreshTokenResponse;
import com.example.shopapp.responses.LoginResponse;
import com.example.shopapp.responses.RegisterResponse;
import com.example.shopapp.responses.ResponseCustom;
import com.example.shopapp.services.MailService;
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
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;


import java.time.Instant;
import java.util.Date;
import java.util.Random;

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
    private final PasswordEncoder passwordEncoder;
    private final MailService mailService;
    private final OTPResetRepository otpResetRepository;

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
        UsernamePasswordAuthenticationToken auth = (UsernamePasswordAuthenticationToken) SecurityContextHolder.getContext().getAuthentication();
        User user = (User) auth.getPrincipal();
        if (!passwordEncoder.matches(changePasswordRequest.getPassword(), user.getPassword())) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new ResponseCustom<>(HttpStatus.BAD_REQUEST.value(), "Password not matches"));
        }
        user.setPassword(passwordEncoder.encode(changePasswordRequest.getNewPassword()));
        userRepository.save(user);
        return ResponseEntity.ok(new ResponseCustom<>(HttpStatus.OK.value(), "Change password successfully"));
    }

    @PostMapping("/forgot/{email}")
    public ResponseEntity<ResponseCustom<?>> verifyEmail(@PathVariable("email") String email) {
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new UsernameNotFoundException("Please provide an valid email!"));

        int otp = generateOTP();

        MailBody mailBody = MailBody.builder()
                .to(email)
                .text("Thank you for requesting to reset your account password." + "\n"
                    + "\nYour OTP code is " + otp + ". \nThis OTP code is valid for 2 minutes."
                    + "\nPlease enter the OTP code to reset the password for your account." + "\n"
                    + "\nNote: This OTP code is only for you and must not be shared with anyone else." + "\n"
                    + "\nBest regards,"
                    + "\nFrom group 1 with love")
                .subject("Account confirmation - OTP")
                .build();

        OTPReset otpReset = OTPReset.builder()
                .otp(otp)
                .expirationDate(new Date(System.currentTimeMillis() + 2 * 60 * 1000)) // 2minutes
                .user(user)
                .build();

        mailService.sendSimpleMessage(mailBody);
        otpResetRepository.save(otpReset);
        return ResponseEntity.ok(new ResponseCustom<>(HttpStatus.OK.value(), "Please check email to have OTP code"));
    }

    @PostMapping("/update-password/{email}")
    public ResponseEntity<ResponseCustom<?>> updatePassword(@Valid UpdatePasswordRequest updatePasswordRequest, @Valid @PathVariable("email") String email) {
        String encodedNewPassword = passwordEncoder.encode(updatePasswordRequest.getNewPassword());
        userRepository.updatePassword(email, encodedNewPassword);
        return ResponseEntity.ok(new ResponseCustom<>(HttpStatus.OK.value(), "Password has been changed"));
    }

    @PostMapping("/verifyOtp/{otp}/{email}")
    public ResponseEntity<ResponseCustom<?>> verifyOtp(@Valid @PathVariable int otp, @Valid @PathVariable String email) {
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new UsernameNotFoundException("Please provide an valid email!"));
        OTPReset otpReset = otpResetRepository.findByOtpAndUser(otp, user)
                .orElseThrow(() -> new UsernameNotFoundException("Invalid OTP for email " + email));
        if (otpReset.getExpirationDate().before(Date.from(Instant.now()))) {
            otpResetRepository.deleteById(otpReset.getId());
            return ResponseEntity.status(HttpStatus.EXPECTATION_FAILED).body(new ResponseCustom<>(HttpStatus.EXPECTATION_FAILED.value(), "OTP has expired!"));
        }
        return ResponseEntity.ok(new ResponseCustom<>(HttpStatus.OK.value(), "OTP verified!"));
    }

    private Integer generateOTP() {
        Random random = new Random();
        return random.nextInt(100_000, 999_999);
    }
}
