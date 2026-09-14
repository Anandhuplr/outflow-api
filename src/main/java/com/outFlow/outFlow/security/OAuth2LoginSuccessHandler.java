package com.outFlow.outFlow.security;



import com.outFlow.outFlow.entity.User;
import com.outFlow.outFlow.service.UserService;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseCookie;
import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

import java.io.IOException;

@Component
public class OAuth2LoginSuccessHandler
        implements AuthenticationSuccessHandler {

    private final UserService userService;
    private final JwtService jwtService;

    @Value("${app.frontend-url}")
    private String frontendUrl;

    public OAuth2LoginSuccessHandler(
            UserService userService,
            JwtService jwtService) {

        this.userService = userService;
        this.jwtService = jwtService;
    }

    @Override
    public void onAuthenticationSuccess(
            HttpServletRequest request,
            HttpServletResponse response,
            Authentication authentication)
            throws IOException, ServletException {

        OAuth2User oauthUser =
                (OAuth2User) authentication.getPrincipal();

        User user =
                userService.processOAuthUser(oauthUser);

        String jwt =
                jwtService.generateToken(user.getId());

        ResponseCookie cookie =
                ResponseCookie.from("access_token", jwt)
                        .httpOnly(true)
                        .secure(false) // true in production HTTPS
                        .sameSite("Lax")
                        .path("/")
                        .maxAge(24 * 60 * 60)
                        .build();

        response.addHeader(
                HttpHeaders.SET_COOKIE,
                cookie.toString()
        );

        response.sendRedirect(
                frontendUrl + "/dashboard"
        );
    }
}