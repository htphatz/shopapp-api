package com.example.shopapp.controllers;

import com.example.shopapp.components.JwtTokenUtils;
import com.example.shopapp.dtos.RefreshTokenRequest;
import com.example.shopapp.dtos.UserDTO;
import com.example.shopapp.dtos.UserLoginDTO;
import com.example.shopapp.exceptions.RefreshTokenExpiredException;
import com.example.shopapp.exceptions.RefreshTokenNotFoundException;
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
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

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
    public ResponseEntity<RegisterResponse> createUser(UserDTO userDTO) {
        User user = new User();
        user.setRole(new Role(1L, Role.USER));
        try {
            user = userService.createUser(userDTO);
            return  ResponseEntity.ok(new RegisterResponse(SUCCESS, USER_CREATED_SUCCESS));
        } catch (DataIntegrityViolationException e) {
            return  ResponseEntity.status(HttpStatus.OK).body(new RegisterResponse(EMAIL_DUPLICATED_CODE, USER_DUPLICATED_EMAIL));
        } catch (Exception e) {
            return  ResponseEntity.internalServerError().body(new RegisterResponse("INTERNAL_SERVER_ERROR", e.getMessage()));
        }
    }
//    @PostMapping("/register")
//    public ResponseEntity<RegisterResponse> createUser(
//            UserDTO userDTO,
//            BindingResult result) {
//        try {
//            if (result.hasErrors()) {
//                List<String> errorMessages = result.getFieldErrors()
//                        .stream()
//                        .map(FieldError::getDefaultMessage)
//                        .toList();
//                return ResponseEntity.badRequest().body(
//                        RegisterResponse.builder()
//                                .status(HttpStatus.BAD_REQUEST.value())
//                                .message("Register failed")
//                                .build()
//                );
//            }
//            if(!userDTO.getPassword().equals(userDTO.getRetypePassword())) {
//                return  ResponseEntity.status(HttpStatus.BAD_REQUEST).body(RegisterResponse.builder()
//                                .status(HttpStatus.BAD_REQUEST.value())
//                                .message("Password not match")
//                                .build());
////                return ResponseEntity.badRequest().body(
////                        RegisterResponse.builder()
////                                .status(HttpStatus.UNAUTHORIZED.value())
////                                .message("Password not match")
////                                .build()
////                );
//            }
//            User user = userService.createUser(userDTO);
//            return ResponseEntity.ok(RegisterResponse.builder()
//                            .status(HttpStatus.CREATED.value())
//                            .message("Register successfully")
//                            .user(user)
//                    .build());
//        }
//        catch (DataIntegrityViolationException e) {
//            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(RegisterResponse.builder()
//                    .status(HttpStatus.BAD_REQUEST.value())
//                    .message("Email is already exists")
//                    .build());
//        }
//        catch (Exception e) {
//            return ResponseEntity.badRequest().body(
//                    RegisterResponse.builder()
//                            .status(HttpStatus.INTERNAL_SERVER_ERROR.value())
//                            .message(e.getMessage())
//                            .build()
//            );
//        }
//    }

    @PostMapping("/login")
    public ResponseEntity<?> login(
            UserLoginDTO userLoginDTO,
            BindingResult result) throws Exception {
        // Kiem tra thong tin dang nhap
        // Tra ve thong tin dang nhap va sinh token
        try {
            if (result.hasErrors()) {
                List<String> errorMessages = result.getFieldErrors()
                        .stream()
                        .map(FieldError::getDefaultMessage)
                        .toList();
                return ResponseEntity.badRequest().body(errorMessages);
            }
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
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(LoginResponse.builder()
                        .message(localizationUtils.getLocalizedMessage(MessageKeys.LOGIN_FAILED, e.getMessage()))
                    .build()
            );
        }
    }

    @PostMapping("/refreshToken")
    public ResponseEntity<?> refreshToken(@Valid RefreshTokenRequest
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
            return ResponseEntity.ok(RefreshTokenResponse.builder()
                    .accessToken(accessToken)
                    .refreshToken(refreshToken.getToken())
                    .build());
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @GetMapping("/me")
    public ResponseEntity<?> getInformation() throws Exception {
        UsernamePasswordAuthenticationToken auth = (UsernamePasswordAuthenticationToken) SecurityContextHolder.getContext().getAuthentication();
        return ResponseEntity.ok(auth.getPrincipal());
    }

}
