package com.example.shopapp.exceptions;

import com.example.shopapp.responses.ErrorResponse;
import com.example.shopapp.responses.RegisterResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.time.ZonedDateTime;

import static com.example.shopapp.constants.UserConstant.EMAIL_DUPLICATED_CODE;
import static com.example.shopapp.constants.UserConstant.USER_DUPLICATED_EMAIL;

@Slf4j
@RestControllerAdvice
public class GlobalExceptionHandler {
    @ExceptionHandler(ResourceNotFoundException.class)
    public ResponseEntity<ErrorResponse> handleResourceNotFoundException(ResourceNotFoundException e) {
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new ErrorResponse(HttpStatus.NOT_FOUND.value(), e.getMessage(), ZonedDateTime.now().toEpochSecond()));
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ErrorResponse> handleMethodArgumentNotValidException(MethodArgumentNotValidException e) {
        log.error("Validation error: {}", e.getMessage());
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new ErrorResponse(HttpStatus.BAD_REQUEST.value(), HttpStatus.BAD_REQUEST.getReasonPhrase(), ZonedDateTime.now().toEpochSecond()));
    }

    @ExceptionHandler(RefreshTokenExpiredException.class)
    public ResponseEntity<ErrorResponse> handleRefreshTokenExpiredException(RefreshTokenExpiredException e) {
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(new ErrorResponse(HttpStatus.UNAUTHORIZED.value(), e.getMessage(), ZonedDateTime.now().toEpochSecond()));
    }

    @ExceptionHandler(BadCredentialsException.class)
    public ResponseEntity<ErrorResponse> handleBadCredentialsException(BadCredentialsException e) {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new ErrorResponse(HttpStatus.BAD_REQUEST.value(), e.getMessage(), ZonedDateTime.now().toEpochSecond()));
    }

    @ExceptionHandler(DataIntegrityViolationException.class)
    public ResponseEntity<RegisterResponse> handleDataIntegrityViolationException(DataIntegrityViolationException e) {
        return  ResponseEntity.status(HttpStatus.OK).body(new RegisterResponse(EMAIL_DUPLICATED_CODE, USER_DUPLICATED_EMAIL));
    }

    @ExceptionHandler(PermissionDenyException.class)
    public ResponseEntity<ErrorResponse> handlePermissionDenyException(PermissionDenyException e) {
        return ResponseEntity.status(HttpStatus.FORBIDDEN).body(new ErrorResponse(HttpStatus.FORBIDDEN.value(), e.getMessage(), ZonedDateTime.now().toEpochSecond()));
    }

    @ExceptionHandler(UsernameNotFoundException.class)
    public ResponseEntity<ErrorResponse> handleUsernameNotFoundException(UsernameNotFoundException e) {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new ErrorResponse(HttpStatus.BAD_REQUEST.value(), e.getMessage(), ZonedDateTime.now().toEpochSecond()));
    }

    @ExceptionHandler(OTPExpiredException.class)
    public ResponseEntity<ErrorResponse> handleOTPExpiredException(OTPExpiredException e) {
        return ResponseEntity.status(HttpStatus.EXPECTATION_FAILED).body(new ErrorResponse(HttpStatus.EXPECTATION_FAILED.value(), e.getMessage(), ZonedDateTime.now().toEpochSecond()));
    }
}
