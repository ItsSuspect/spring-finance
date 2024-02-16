package com.web.finance.security.services;

import java.sql.Ref;
import java.time.Instant;
import java.util.Optional;
import java.util.UUID;

import com.web.finance.entities.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.web.finance.exception.TokenRefreshException;
import com.web.finance.entities.RefreshToken;
import com.web.finance.repository.RefreshTokenRepository;
import com.web.finance.repository.UserRepository;

@Service
public class RefreshTokenService {
    @Value("${finance.app.jwtRefreshExpirationMs}")
    private Long refreshTokenDurationMs;

    @Value("${finance.app.refreshTokenCookieName}")
    private String refreshCookieName;

    @Autowired
    private RefreshTokenRepository refreshTokenRepository;

    @Autowired
    private UserRepository userRepository;

    public User findUserByToken(String refreshToken) {
        return refreshTokenRepository.findUserByToken(refreshToken);
    }

    //Todo: Добавить обработчик ошибок при ненахождение токена
    public RefreshToken findByToken(String token) {
        return refreshTokenRepository.findByToken(token).get();
    }

    public Optional<RefreshToken> findTokenByUser(User user) {
        return refreshTokenRepository.findRefreshTokenByUser(user);
    }

    public RefreshToken updateRefreshToken(RefreshToken refreshToken) {
        refreshToken.setExpiryDate(Instant.now().plusMillis(refreshTokenDurationMs));
        refreshToken.setToken(UUID.randomUUID().toString());

        refreshTokenRepository.save(refreshToken);
        return refreshToken;
    }

    public RefreshToken createRefreshToken(Long userId) {
        User user = userRepository.findById(userId).get();
        RefreshToken refreshToken = new RefreshToken();
        Optional<RefreshToken> existingToken = findTokenByUser(user);

        if (existingToken.isPresent()) {
            refreshToken = updateRefreshToken(existingToken.get());
        } else {
            refreshToken.setUser(user);
            refreshToken.setExpiryDate(Instant.now().plusMillis(refreshTokenDurationMs));
            refreshToken.setToken(UUID.randomUUID().toString());

            refreshToken = refreshTokenRepository.save(refreshToken);
        }
        return refreshToken;
    }

    public boolean isValidRefreshToken(String token) {
        if (token == null) return false;
        RefreshToken refreshToken = findByToken(token);
        if (refreshToken.getExpiryDate().compareTo(Instant.now()) < 0) {
            refreshTokenRepository.delete(refreshToken);
            return false;
        }
        return true;
    }

    @Transactional
    public void deleteRefreshToken(RefreshToken refreshToken) {
        refreshTokenRepository.delete(refreshToken);
    }
}
