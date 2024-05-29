package com.example.shopapp.exceptions;

public class OTPExpiredException extends RuntimeException {
    public OTPExpiredException(String message) {
        super(message);
    }
}
