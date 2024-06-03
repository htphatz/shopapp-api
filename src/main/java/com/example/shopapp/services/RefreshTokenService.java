package com.example.shopapp.services;

import com.example.shopapp.entities.RefreshToken;
import com.example.shopapp.repositories.RefreshTokenRepository;
import com.example.shopapp.repositories.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.Optional;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class RefreshTokenService {
    private final UserRepository userRepository;
    private final RefreshTokenRepository refreshTokenRepository;

    @Value("${jwt.refresh-expiration}")
    private int refreshExpiration;

    public RefreshToken createRefreshToken(String email) {
        RefreshToken refreshToken =  RefreshToken.builder()
                .user(userRepository.findByEmail(email).get()) // Optional<User>
                .token(UUID.randomUUID().toString())
                .expirationDate(Instant.now().plusMillis(refreshExpiration * 1000L))
                .revoked(false)
                .expired(false)
                .build();
        return refreshTokenRepository.save(refreshToken);
    }

    public Optional<RefreshToken> findByToken(String token) {
        return refreshTokenRepository.findByToken(token);
    }

    public RefreshToken verifyExpiration(RefreshToken refreshToken) {
        if (refreshToken.getExpirationDate().compareTo(Instant.now()) < 0) {
            refreshToken.setRevoked(true);
            refreshToken.setExpired(true);
            return refreshTokenRepository.save(refreshToken);
        }
        return refreshToken;
    }
}
