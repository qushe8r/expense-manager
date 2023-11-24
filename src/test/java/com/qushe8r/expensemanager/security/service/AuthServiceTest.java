package com.qushe8r.expensemanager.security.service;

import static org.junit.jupiter.api.Assertions.*;

import com.qushe8r.expensemanager.matcher.MemberMatcher;
import com.qushe8r.expensemanager.member.entity.Member;
import com.qushe8r.expensemanager.member.exception.MemberNotFoundException;
import com.qushe8r.expensemanager.member.service.MemberService;
import com.qushe8r.expensemanager.security.exception.TokenNotFoundException;
import com.qushe8r.expensemanager.security.jwt.RefreshToken;
import com.qushe8r.expensemanager.security.jwt.TokenProvider;
import com.qushe8r.expensemanager.security.repository.RefreshTokenRepository;
import com.qushe8r.expensemanager.security.utils.CookieProperties;
import com.qushe8r.expensemanager.stub.JwtFactory;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import java.nio.charset.StandardCharsets;
import java.util.Base64;
import java.util.Optional;
import javax.crypto.SecretKey;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.BDDMockito;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpHeaders;

@ExtendWith(MockitoExtension.class)
class AuthServiceTest {

  @Mock private TokenProvider tokenProvider;

  @Mock private MemberService memberService;

  @Mock private RefreshTokenRepository refreshTokenRepository;

  @Mock private CookieProperties cookieProperties;

  @InjectMocks private AuthService authService;

  @Test
  void reissue() {
    // given
    String jti = "jti";
    String token = JwtFactory.withDefaultValues().generateStubToken();
    String email = "test@email.com";
    Long expiration = 0L;

    Long memberId = 1L;
    String password = "encodedPassword";

    Member member = new Member(memberId, email, password, false, false);

    byte[] base64EncodedSecret =
        Base64.getEncoder().encode(JwtFactory.TEST_SECRET.getBytes(StandardCharsets.UTF_8));
    SecretKey secretKey = Keys.hmacShaKeyFor(base64EncodedSecret);

    Claims claims =
        Jwts.parser().verifyWith(secretKey).build().parseSignedClaims(token).getPayload();

    String newAccessToken = "newAccessToken";
    String newRefreshToken = "newRefreshToken";

    String domain = "domain";

    BDDMockito.given(tokenProvider.extractClaims(token)).willReturn(claims);
    BDDMockito.given(refreshTokenRepository.findById(jti))
        .willReturn(Optional.of(new RefreshToken(jti, token, email, expiration)));
    BDDMockito.doNothing().when(refreshTokenRepository).delete(Mockito.any(RefreshToken.class));
    BDDMockito.given(memberService.validateMemberExistByEmailOrElseThrow(email)).willReturn(member);
    BDDMockito.given(
            tokenProvider.generateBearerAccessToken(
                Mockito.eq(jti), Mockito.argThat(new MemberMatcher(member))))
        .willReturn(newAccessToken);
    BDDMockito.given(tokenProvider.generateRefreshToken(jti, email)).willReturn(newRefreshToken);
    BDDMockito.given(cookieProperties.getDomain()).willReturn(domain);

    // when
    HttpHeaders result = authService.reissue(token);

    // then
    Assertions.assertThat(result)
        .hasFieldOrProperty(HttpHeaders.AUTHORIZATION)
        .hasFieldOrProperty(HttpHeaders.SET_COOKIE);
  }

  @Test
  void reissueTokenNotFoundException() {
    // given
    String jti = "jti";
    String token = JwtFactory.withDefaultValues().generateStubToken();

    byte[] base64EncodedSecret =
        Base64.getEncoder().encode(JwtFactory.TEST_SECRET.getBytes(StandardCharsets.UTF_8));
    SecretKey secretKey = Keys.hmacShaKeyFor(base64EncodedSecret);

    Claims claims =
        Jwts.parser().verifyWith(secretKey).build().parseSignedClaims(token).getPayload();

    BDDMockito.given(tokenProvider.extractClaims(token)).willReturn(claims);
    BDDMockito.given(refreshTokenRepository.findById(jti)).willReturn(Optional.empty());

    // when & then
    Assertions.assertThatThrownBy(() -> authService.reissue(token))
        .isInstanceOf(TokenNotFoundException.class);
  }

  @Test
  void reissueMemberNotFoundException() {
    // given
    String jti = "jti";
    String token = JwtFactory.withDefaultValues().generateStubToken();
    String email = "test@email.com";
    Long expiration = 0L;

    byte[] base64EncodedSecret =
        Base64.getEncoder().encode(JwtFactory.TEST_SECRET.getBytes(StandardCharsets.UTF_8));
    SecretKey secretKey = Keys.hmacShaKeyFor(base64EncodedSecret);

    Claims claims =
        Jwts.parser().verifyWith(secretKey).build().parseSignedClaims(token).getPayload();

    BDDMockito.given(tokenProvider.extractClaims(token)).willReturn(claims);
    BDDMockito.given(refreshTokenRepository.findById(jti))
        .willReturn(Optional.of(new RefreshToken(jti, token, email, expiration)));
    BDDMockito.doNothing().when(refreshTokenRepository).delete(Mockito.any(RefreshToken.class));
    BDDMockito.given(memberService.validateMemberExistByEmailOrElseThrow(email))
        .willThrow(new MemberNotFoundException());

    // when & then
    Assertions.assertThatThrownBy(() -> authService.reissue(token))
        .isInstanceOf(MemberNotFoundException.class);
  }
}
