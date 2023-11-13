package com.qushe8r.expensemanager.security.handler;

import com.qushe8r.expensemanager.security.jwt.TokenProvider;
import com.qushe8r.expensemanager.security.repository.RefreshTokenRepository;
import com.qushe8r.expensemanager.security.utils.CookieCreator;
import io.jsonwebtoken.Claims;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.logout.LogoutSuccessHandler;
import org.springframework.stereotype.Component;

@Component
public class JwtLogoutHandler implements LogoutSuccessHandler {

  private final TokenProvider tokenProvider;

  private final RefreshTokenRepository refreshTokenRepository;

  private final HttpStatus httpStatus;

  public JwtLogoutHandler(
      TokenProvider tokenProvider, RefreshTokenRepository refreshTokenRepository) {
    this.tokenProvider = tokenProvider;
    this.refreshTokenRepository = refreshTokenRepository;
    this.httpStatus = HttpStatus.OK;
  }

  @Override
  public void onLogoutSuccess(
      HttpServletRequest request, HttpServletResponse response, Authentication authentication) {
    String accessToken =
        request.getHeader(HttpHeaders.AUTHORIZATION).substring(TokenProvider.BEARER.length());
    Claims claims = tokenProvider.extractClaims(accessToken);
    String jti = claims.getId();
    refreshTokenRepository.deleteById(jti);
    Cookie cookie = CookieCreator.createCookie(null, null, 0);
    response.addCookie(cookie);
    response.setStatus(this.httpStatus.value());
  }
}
