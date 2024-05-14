package com.example.shopapp.exceptions;

public class RefreshTokenExpiredException extends Exception {
    public RefreshTokenExpiredException(String message) {
        super(message);
    }
}
