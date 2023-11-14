package com.qushe8r.expensemanager.common.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.qushe8r.expensemanager.security.filter.JwtAuthenticationFilter;
import com.qushe8r.expensemanager.security.filter.JwtVerificationFilter;
import com.qushe8r.expensemanager.security.handler.JwtAuthenticationFailureHandler;
import com.qushe8r.expensemanager.security.jwt.TokenProvider;
import com.qushe8r.expensemanager.security.repository.RefreshTokenRepository;
import com.qushe8r.expensemanager.security.utils.CookieProperties;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class JwtFilterConfig extends AbstractHttpConfigurer<JwtFilterConfig, HttpSecurity> {

  private final TokenProvider tokenProvider;

  private final RefreshTokenRepository refreshTokenRepository;

  private final CookieProperties cookieProperties;

  private final ObjectMapper objectMapper;

  private final JwtAuthenticationFailureHandler jwtAuthenticationFailureHandler;

  @Override
  public void configure(HttpSecurity builder) {
    AuthenticationManager authenticationManager =
        builder.getSharedObject(AuthenticationManager.class);
    JwtAuthenticationFilter jwtAuthenticationFilter =
        new JwtAuthenticationFilter(
            tokenProvider, refreshTokenRepository, cookieProperties, objectMapper);
    JwtVerificationFilter jwtVerificationFilter = new JwtVerificationFilter(tokenProvider);
    jwtAuthenticationFilter.setAuthenticationManager(authenticationManager);
    jwtAuthenticationFilter.setFilterProcessesUrl("/sign-in");
    jwtAuthenticationFilter.setAuthenticationFailureHandler(jwtAuthenticationFailureHandler);

    builder.addFilterAt(jwtAuthenticationFilter, UsernamePasswordAuthenticationFilter.class);
    builder.addFilterBefore(jwtVerificationFilter, JwtAuthenticationFilter.class);
  }
}
