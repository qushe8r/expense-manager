package com.qushe8r.expensemanager.security.service;

import com.qushe8r.expensemanager.member.entity.Member;
import com.qushe8r.expensemanager.member.service.MemberService;
import com.qushe8r.expensemanager.security.exception.TokenNotFoundException;
import com.qushe8r.expensemanager.security.jwt.RefreshToken;
import com.qushe8r.expensemanager.security.jwt.TokenProvider;
import com.qushe8r.expensemanager.security.repository.RefreshTokenRepository;
import com.qushe8r.expensemanager.security.utils.CookieCreator;
import com.qushe8r.expensemanager.security.utils.CookieProperties;
import io.jsonwebtoken.Claims;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseCookie;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AuthService {

  private final TokenProvider tokenProvider;

  private final MemberService memberService;

  private final RefreshTokenRepository refreshTokenRepository;

  private final CookieProperties cookieProperties;

  public HttpHeaders reissue(String refreshToken) {
    Claims claims = tokenProvider.extractClaims(refreshToken);
    String jti = claims.getId();
    String email = claims.getSubject();

    RefreshToken refresh =
        refreshTokenRepository.findById(jti).orElseThrow(TokenNotFoundException::new);
    refreshTokenRepository.delete(refresh);
    Member member = memberService.validateMemberExistByEmailOrElseThrow(email);

    String newBearerToken = tokenProvider.generateBearerAccessToken(jti, member);
    String newRefreshToken = tokenProvider.generateRefreshToken(jti, email);

    refreshTokenRepository.save(
        new RefreshToken(
            jti, newRefreshToken, email, Long.valueOf(tokenProvider.refreshExpirationSeconds())));

    ResponseCookie cookie =
        CookieCreator.createResponseCookie(
            refreshToken, cookieProperties.getDomain(), tokenProvider.refreshExpirationSeconds());

    HttpHeaders httpHeaders = new HttpHeaders();
    httpHeaders.add(HttpHeaders.AUTHORIZATION, newBearerToken);
    httpHeaders.add(HttpHeaders.SET_COOKIE, cookie.toString());

    return httpHeaders;
  }
}
