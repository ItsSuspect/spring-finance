package com.web.finance.security.jwt;

import java.util.Date;

import com.web.finance.entities.RefreshToken;
import com.web.finance.security.services.UserDetailsImpl;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseCookie;
import org.springframework.stereotype.Component;

import io.jsonwebtoken.*;
import org.springframework.web.util.WebUtils;

@Component
public class JwtUtils {
    private static final Logger logger = LoggerFactory.getLogger(JwtUtils.class);

    @Value("${finance.app.jwtSecret}")
    private String jwtSecret;

    @Value("${finance.app.jwtExpirationMs}")
    private int jwtExpirationMs;

    @Value("${finance.app.jwtRefreshExpirationMs}")
    private int jwtRefreshExpirationMs;

    @Value("${finance.app.jwtCookieName}")
    private String jwtCookieName;

    @Value("${finance.app.refreshTokenCookieName}")
    private String refreshCookieName;

    private String getCookieValueByName(HttpServletRequest request, String name) {
        Cookie cookie = WebUtils.getCookie(request, name);
        if (cookie != null) {
            return cookie.getValue();
        } else {
            return null;
        }
    }

    public Cookie updateJwtTokenCookie(UserDetailsImpl userPrincipal) {
        String newJwt = generateJwtTokenFromUsername(userPrincipal.getUsername());

        Cookie cookie = new Cookie(jwtCookieName, newJwt);
        cookie.setMaxAge(jwtExpirationMs/1000);
        cookie.setPath("/");
        cookie.setHttpOnly(true);

        return cookie;
    }

    public Cookie updateRefreshTokenCookie(Cookie cookie, String refreshToken) {
        cookie.setValue(refreshToken);
        cookie.setMaxAge(jwtRefreshExpirationMs/1000);
        cookie.setHttpOnly(true);
        cookie.setPath("/");

        return cookie;
    }

    public String getJwtFromCookies(HttpServletRequest request) {
        return getCookieValueByName(request, jwtCookieName);
    }

    public String getJwtRefreshFromCookies(HttpServletRequest request) {
        return getCookieValueByName(request, refreshCookieName);
    }

    public ResponseCookie generateJwtCookie(UserDetailsImpl userPrincipal) {
        String jwt = generateJwtTokenFromUsername(userPrincipal.getUsername());
        return generateCookie(jwtCookieName, jwt, "/", jwtExpirationMs);
    }

    public ResponseCookie generateRefreshJwtCookie(String refreshToken) {
        return generateCookie(refreshCookieName, refreshToken, "/", jwtRefreshExpirationMs);
    }

    private ResponseCookie generateCookie(String name, String value, String path, int expirationMs) {
        return ResponseCookie.from(name, value)
                .path(path)
                .maxAge(expirationMs/1000)
                .httpOnly(true)
                .build();
    }

    public String generateJwtTokenFromUsername(String username) {
        return Jwts.builder()
                .setSubject(username)
                .setIssuedAt(new Date())
                .setExpiration(new Date((new Date()).getTime() + jwtExpirationMs))
                .signWith(SignatureAlgorithm.HS512, jwtSecret)
                .compact();
    }

    public String getUsernameFromJwtToken(String token) {
        return Jwts.parser()
                .setSigningKey(jwtSecret)
                .parseClaimsJws(token)
                .getBody()
                .getSubject();
    }

    public boolean validateJwtToken(String authToken) {
        try {
            Jwts.parser().setSigningKey(jwtSecret).parseClaimsJws(authToken);
            return true;
        } catch (SignatureException e) {
            logger.error("Invalid JWT signature: {}", e.getMessage());
        } catch (MalformedJwtException e) {
            logger.error("Invalid JWT token: {}", e.getMessage());
        } catch (ExpiredJwtException e) {
            logger.error("JWT token is expired: {}", e.getMessage());
        } catch (UnsupportedJwtException e) {
            logger.error("JWT token is unsupported: {}", e.getMessage());
        } catch (IllegalArgumentException e) {
            logger.error("JWT claims string is empty: {}", e.getMessage());
        }
        return false;
    }
}
