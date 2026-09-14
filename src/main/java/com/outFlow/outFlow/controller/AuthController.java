package com.outFlow.outFlow.controller;

import com.outFlow.outFlow.entity.User;
import com.outFlow.outFlow.service.UserService;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.http.ResponseCookie;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/auth")
public class AuthController {

    private final UserService userService;

    public AuthController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping("/me")
    public User getCurrentUser(
            Authentication authentication) {

        if (authentication == null || authentication.getPrincipal() == null) {
            throw new org.springframework.web.server.ResponseStatusException(
                    org.springframework.http.HttpStatus.UNAUTHORIZED,
                    "User is not authenticated"
            );
        }

        Object principal = authentication.getPrincipal();
        Long userId;

        if (principal instanceof Long) {
            userId = (Long) principal;
        } else if (principal instanceof String) {
            try {
                userId = Long.valueOf((String) principal);
            } catch (NumberFormatException ex) {
                throw new org.springframework.web.server.ResponseStatusException(
                        org.springframework.http.HttpStatus.INTERNAL_SERVER_ERROR,
                        "Invalid principal type"
                );
            }
        } else {
            throw new org.springframework.web.server.ResponseStatusException(
                    org.springframework.http.HttpStatus.INTERNAL_SERVER_ERROR,
                    "Unsupported principal type"
            );
        }

        return userService.getUserById(userId);
    }

    @PostMapping("/logout")
    public void logout(
            HttpServletResponse response) {

        ResponseCookie cookie =
                ResponseCookie.from("access_token", "")
                        .httpOnly(true)
                        .secure(false)
                        .sameSite("Lax")
                        .path("/")
                        .maxAge(0)
                        .build();

        response.addHeader(
                "Set-Cookie",
                cookie.toString()
        );
    }
}