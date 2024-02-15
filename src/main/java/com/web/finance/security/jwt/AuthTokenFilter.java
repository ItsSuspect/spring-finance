package com.web.finance.security.jwt;

import java.io.IOException;
import java.util.Arrays;

import com.web.finance.entities.RefreshToken;
import com.web.finance.entities.User;
import com.web.finance.security.services.RefreshTokenService;
import com.web.finance.security.services.UserDetailsImpl;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import com.web.finance.security.services.UserDetailsServiceImpl;
import jakarta.validation.constraints.NotNull;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.OncePerRequestFilter;
import org.springframework.web.util.WebUtils;

public class AuthTokenFilter extends OncePerRequestFilter {
    @Autowired
    private JwtUtils jwtUtils;

    @Autowired
    private UserDetailsServiceImpl userDetailsService;

    @Autowired
    private RefreshTokenService refreshTokenService;

    private static final Logger logger = LoggerFactory.getLogger(AuthTokenFilter.class);

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        try {
            String jwt = parseJwtToken(request);
            String refreshToken = parseRefreshToken(request);

            authenticationUser(request, jwt);

            if (refreshTokenService.isValidRefreshToken(refreshToken)){
                User user = refreshTokenService.findUserByToken(refreshToken);
                String username = user.getUsername();
                UserDetailsImpl userDetails = (UserDetailsImpl) userDetailsService.loadUserByUsername(username);

                Cookie cookieJwt = jwtUtils.updateJwtTokenCookie(userDetails);
                response.addCookie(cookieJwt);

                Cookie cookieRefresh = WebUtils.getCookie(request, "RefreshToken");
                if (cookieRefresh != null) {
                    RefreshToken newRefreshToken = refreshTokenService.updateRefreshToken(refreshTokenService.findByToken(refreshToken));
                    response.addCookie(jwtUtils.updateRefreshTokenCookie(cookieRefresh, newRefreshToken.getToken()));
                }

                authenticationUser(request, cookieJwt.getValue());
            }
        } catch (Exception e) {
            logger.error("Cannot set user authentication: {}", e.getMessage());
        }

        filterChain.doFilter(request, response);
    }

    private void authenticationUser(HttpServletRequest request, String jwt) {
        if (jwt != null && jwtUtils.validateJwtToken(jwt)) {
            String username = jwtUtils.getUsernameFromJwtToken(jwt);

            UserDetails userDetails = userDetailsService.loadUserByUsername(username);
            UsernamePasswordAuthenticationToken authentication = new UsernamePasswordAuthenticationToken(userDetails, null,
                    userDetails.getAuthorities());
            authentication.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));

            SecurityContextHolder.getContext().setAuthentication(authentication);
        }
    }

    private String parseJwtToken(HttpServletRequest request) {
        return jwtUtils.getJwtFromCookies(request);
    }

    private String parseRefreshToken(HttpServletRequest request) {
        return jwtUtils.getJwtRefreshFromCookies(request);
    }
}
