package com.outFlow.outFlow.config;

import com.outFlow.outFlow.security.JwtAuthenticationFilter;
import com.outFlow.outFlow.security.OAuth2LoginSuccessHandler;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;

import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.web.AuthenticationEntryPoint;

import jakarta.servlet.http.HttpServletResponse;

@Configuration
public class SecurityConfig {

    private final JwtAuthenticationFilter jwtAuthenticationFilter;
    private final OAuth2LoginSuccessHandler successHandler;

    public SecurityConfig(
            JwtAuthenticationFilter jwtAuthenticationFilter,
            OAuth2LoginSuccessHandler successHandler) {

        this.jwtAuthenticationFilter = jwtAuthenticationFilter;
        this.successHandler = successHandler;
    }

    @Bean
    public SecurityFilterChain securityFilterChain(
            HttpSecurity http) throws Exception {

        http
                .csrf(csrf -> csrf.disable())

                .cors(cors -> {})

                /*
                 * OAuth2 requires a temporary session during
                 * Google's authentication flow.
                 *
                 * After OAuth2 login, our application uses
                 * JWT authentication.
                 */
                .sessionManagement(session ->
                        session.sessionCreationPolicy(
                                SessionCreationPolicy.IF_REQUIRED
                        )
                )

                .authorizeHttpRequests(auth -> auth

                        // Google OAuth2 flow
                        .requestMatchers(
                                "/oauth2/**",
                                "/login/**"
                        ).permitAll()

                        // Logout
                        .requestMatchers(
                                "/api/auth/logout"
                        ).permitAll()

                        // Current user requires JWT
                        .requestMatchers(
                                "/api/auth/me"
                        ).authenticated()

                        // All other APIs require authentication
                        .anyRequest().authenticated()
                )

                /*
                 * IMPORTANT:
                 *
                 * Don't redirect API requests to Google login.
                 * Return HTTP 401 instead.
                 */
                .exceptionHandling(exception ->
                        exception.authenticationEntryPoint(
                                apiAuthenticationEntryPoint()
                        )
                )

                // Google OAuth2
                .oauth2Login(oauth ->
                        oauth.successHandler(successHandler)
                )

                // JWT authentication
                .addFilterBefore(
                        jwtAuthenticationFilter,
                        UsernamePasswordAuthenticationFilter.class
                );

        return http.build();
    }

    @Bean
    public AuthenticationEntryPoint apiAuthenticationEntryPoint() {

        return (request, response, authException) -> {

            if (request.getRequestURI().startsWith("/api/")) {

                response.setStatus(
                        HttpServletResponse.SC_UNAUTHORIZED
                );

                response.setContentType(
                        "application/json"
                );

                response.getWriter().write(
                        "{\"error\":\"Unauthorized\"}"
                );

                return;
            }

            response.sendRedirect(
                    "/oauth2/authorization/google"
            );
        };
    }
}