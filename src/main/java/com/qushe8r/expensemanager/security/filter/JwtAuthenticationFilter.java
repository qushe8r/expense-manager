package com.qushe8r.expensemanager.security.filter;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.qushe8r.expensemanager.member.entity.MemberDetails;
import com.qushe8r.expensemanager.security.dto.UsernamePassword;
import com.qushe8r.expensemanager.security.jwt.JwtProperties;
import com.qushe8r.expensemanager.security.jwt.RefreshToken;
import com.qushe8r.expensemanager.security.jwt.TokenProvider;
import com.qushe8r.expensemanager.security.repository.RefreshTokenRepository;
import com.qushe8r.expensemanager.security.utils.CookieCreator;
import com.qushe8r.expensemanager.security.utils.CookieProperties;
import jakarta.servlet.FilterChain;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import org.springframework.http.HttpHeaders;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@RequiredArgsConstructor
public class JwtAuthenticationFilter extends UsernamePasswordAuthenticationFilter {

  private final TokenProvider tokenProvider;

  private final JwtProperties jwtProperties;

  private final RefreshTokenRepository refreshTokenRepository;

  private final CookieProperties cookieProperties;

  private final ObjectMapper objectMapper;

  @SneakyThrows
  @Override
  public Authentication attemptAuthentication(
      HttpServletRequest request, HttpServletResponse response) throws AuthenticationException {
    UsernamePassword usernamePassword =
        objectMapper.readValue(request.getInputStream(), UsernamePassword.class);

    UsernamePasswordAuthenticationToken authentication =
        new UsernamePasswordAuthenticationToken(
            usernamePassword.email(), usernamePassword.password());

    return getAuthenticationManager().authenticate(authentication);
  }

  @Override
  protected void successfulAuthentication(
      HttpServletRequest request,
      HttpServletResponse response,
      FilterChain chain,
      Authentication authResult) {
    MemberDetails principal = (MemberDetails) authResult.getPrincipal();
    String jti = UUID.randomUUID().toString();

    String bearerAccessToken =
        tokenProvider.generateBearerAccessToken(jti, principal.getEmail(), principal.claims());

    String refreshToken = tokenProvider.generateRefreshToken(jti, principal.getEmail());
    saveRefreshTokenToRedis(jti, refreshToken, principal);

    Cookie cookie =
        CookieCreator.createCookie(
            refreshToken,
            cookieProperties.getDomain(),
            jwtProperties.getRefreshTokenExpirationMinutes() * 60);

    response.setHeader(HttpHeaders.AUTHORIZATION, bearerAccessToken);
    response.addCookie(cookie);
  }

  private void saveRefreshTokenToRedis(String jti, String refreshToken, MemberDetails principal) {
    refreshTokenRepository.save(
        new RefreshToken(
            jti, refreshToken, principal.getUsername(), tokenProvider.refreshExpiration()));
  }
}
