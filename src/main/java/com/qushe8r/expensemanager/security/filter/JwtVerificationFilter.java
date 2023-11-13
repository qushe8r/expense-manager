package com.qushe8r.expensemanager.security.filter;

import com.qushe8r.expensemanager.member.entity.MemberDetails;
import com.qushe8r.expensemanager.security.exception.JwtExceptionCode;
import com.qushe8r.expensemanager.security.jwt.TokenProvider;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.security.SignatureException;
import jakarta.servlet.FilterChain;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpHeaders;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.filter.OncePerRequestFilter;

@RequiredArgsConstructor
@Slf4j
public class JwtVerificationFilter extends OncePerRequestFilter {

  public static final String EXCEPTION = "exception";

  private final TokenProvider tokenProvider;

  @Override
  protected boolean shouldNotFilter(@NonNull HttpServletRequest request) {
    String bearerToken = getAuthorizationHeaderValue(request);
    return bearerToken == null || !bearerToken.startsWith(TokenProvider.BEARER);
  }

  @Override
  protected void doFilterInternal(
      @NonNull HttpServletRequest request,
      @NonNull HttpServletResponse response,
      @NonNull FilterChain filterChain) {
    try {
      setAuthenticationToContext(request);
    } catch (ExpiredJwtException e) {
      request.setAttribute(EXCEPTION, JwtExceptionCode.ACCESS_TOKEN_EXPIRED);
    } catch (SignatureException e) {
      request.setAttribute(EXCEPTION, JwtExceptionCode.INVALID_TOKEN);
    } catch (JwtException e) {
      log.warn("JwtException: {}", e.getClass());
      request.setAttribute(EXCEPTION, JwtExceptionCode.INVALID_TOKEN);
    }
  }

  private void setAuthenticationToContext(HttpServletRequest request) {
    Authentication authentication = createAuthentication(request);
    SecurityContextHolder.getContext().setAuthentication(authentication);
  }

  private Authentication createAuthentication(HttpServletRequest request) {
    String accessToken =
        getAuthorizationHeaderValue(request).substring(TokenProvider.BEARER.length());
    Claims claims = tokenProvider.extractClaims(accessToken);
    MemberDetails memberDetails = new MemberDetails(claims);
    return new UsernamePasswordAuthenticationToken(
        memberDetails, memberDetails.getPassword(), memberDetails.getAuthorities());
  }

  private String getAuthorizationHeaderValue(HttpServletRequest request) {
    return request.getHeader(HttpHeaders.AUTHORIZATION);
  }
}
