package com.web.finance.security.services;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.logout.LogoutSuccessHandler;
import org.springframework.stereotype.Component;
import org.springframework.web.util.WebUtils;

import java.io.IOException;

@Component
public class CustomLogoutSuccessHandler implements LogoutSuccessHandler {
    @Autowired
    RefreshTokenService refreshTokenService;
    @Override
    public void onLogoutSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws IOException, ServletException {
        Cookie cookie = WebUtils.getCookie(request, "RefreshToken");
        if (cookie != null) {
            String refreshToken = cookie.getValue();
            refreshTokenService.deleteRefreshToken(refreshTokenService.findByToken(refreshToken));
        }
        response.sendRedirect("/auth/signIn");
    }
}