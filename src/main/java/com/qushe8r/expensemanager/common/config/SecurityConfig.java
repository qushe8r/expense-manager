package com.qushe8r.expensemanager.common.config;

import com.qushe8r.expensemanager.security.handler.JwtAuthenticationEntryPoint;
import com.qushe8r.expensemanager.security.handler.JwtLogoutHandler;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.annotation.web.configurers.HeadersConfigurer.FrameOptionsConfig;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class SecurityConfig {

  private final JwtFilterConfig jwtFilterConfig;

  private final JwtAuthenticationEntryPoint jwtAuthenticationEntryPoint;

  private final JwtLogoutHandler jwtLogoutHandler;

  @Bean
  public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
    http.apply(jwtFilterConfig);

    return http.authorizeHttpRequests(authorize -> authorize.anyRequest().permitAll())
        .headers(header -> header.frameOptions(FrameOptionsConfig::sameOrigin))
        .formLogin(AbstractHttpConfigurer::disable)
        .httpBasic(AbstractHttpConfigurer::disable)
        .logout(logout -> logout.logoutSuccessHandler(jwtLogoutHandler).logoutUrl("/logout"))
        .csrf(AbstractHttpConfigurer::disable)
        .sessionManagement(
            session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
        .exceptionHandling(
            exception -> exception.authenticationEntryPoint(jwtAuthenticationEntryPoint))
        .build();
  }
}
