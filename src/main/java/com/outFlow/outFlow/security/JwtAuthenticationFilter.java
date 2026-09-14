package com.outFlow.outFlow.security;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.Collections;
import java.util.List;

@Component
public class JwtAuthenticationFilter
        extends OncePerRequestFilter {

    private final JwtService jwtService;

    public JwtAuthenticationFilter(
            JwtService jwtService) {

        this.jwtService = jwtService;
    }

    @Override
    protected void doFilterInternal(
            HttpServletRequest request,
            HttpServletResponse response,
            FilterChain filterChain)
            throws ServletException, IOException {

        System.out.println(
                "JWT FILTER request: "
                        + request.getCookies()
        );
        System.out.println("Origin header: " + request.getHeader("Origin"));

        String token =
                extractToken(request);

        if (token == null) {

            System.out.println(
                    "JWT FILTER: No access_token cookie"
            );

            filterChain.doFilter(
                    request,
                    response
            );

            return;
        }

        System.out.println(
                "JWT FILTER: Token found"
        );

        if (!jwtService.isValid(token)) {

            System.out.println(
                    "JWT FILTER: Invalid JWT"
            );

            filterChain.doFilter(
                    request,
                    response
            );

            return;
        }

        Long userId =
                jwtService.extractUserId(token);

        System.out.println(
                "JWT FILTER: Authenticated user "
                        + userId
        );

        UsernamePasswordAuthenticationToken authentication =
                new UsernamePasswordAuthenticationToken(
                        userId,
                        null,
                        List.of(
                                new SimpleGrantedAuthority(
                                        "ROLE_USER"
                                )
                        )
                );

        SecurityContextHolder
                .getContext()
                .setAuthentication(
                        authentication
                );

        filterChain.doFilter(
                request,
                response
        );
    }

    private String extractToken(
            HttpServletRequest request) {

        Cookie[] cookies =
                request.getCookies();

        if (cookies == null) {
            return null;
        }

        for (Cookie cookie : cookies) {

            System.out.println(
                    "COOKIE: "
                            + cookie.getName()
            );

            if ("access_token"
                    .equals(cookie.getName())) {

                return cookie.getValue();
            }
        }

        return null;
    }
}
