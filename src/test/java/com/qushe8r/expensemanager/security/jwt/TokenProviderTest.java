package com.qushe8r.expensemanager.security.jwt;

import com.qushe8r.expensemanager.member.entity.Member;
import com.qushe8r.expensemanager.stub.JwtFactory;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import io.jsonwebtoken.security.SignatureException;
import java.nio.charset.StandardCharsets;
import java.util.Base64;
import java.util.Map;
import javax.crypto.SecretKey;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.junit.jupiter.SpringExtension;

@ExtendWith(SpringExtension.class)
@EnableConfigurationProperties(value = JwtProperties.class)
@Import(TokenProvider.class)
class TokenProviderTest {

  @SuppressWarnings("SpringJavaInjectionPointsAutowiringInspection")
  @Autowired
  private TokenProvider tokenProvider;

  @SuppressWarnings("SpringJavaInjectionPointsAutowiringInspection")
  @Autowired
  private JwtProperties jwtProperties;

  @Test
  void generateAccessTokenByMember() {
    // given
    String jti = "jti";
    Member member = new Member(1L, "test@email.com", "password");

    byte[] base64EncodedSecret =
        Base64.getEncoder().encode(jwtProperties.getSecret().getBytes(StandardCharsets.UTF_8));
    SecretKey secretKey = Keys.hmacShaKeyFor(base64EncodedSecret);

    String bearerAccessToken = tokenProvider.generateBearerAccessToken(jti, member);
    String accessToken = bearerAccessToken.substring("Bearer ".length());

    // when
    Claims result =
        Jwts.parser().verifyWith(secretKey).build().parseSignedClaims(accessToken).getPayload();

    // then
    Assertions.assertThat(result)
        .hasFieldOrPropertyWithValue("id", jti)
        .hasFieldOrPropertyWithValue("subject", member.getEmail());
    Assertions.assertThat(result.get("id", Long.class)).isEqualTo(member.getId());
  }

  @Test
  void generateAccessTokenBy() {
    // given
    String jti = "jti";
    Long memberId = 1L;
    String email = "test@email.com";

    Map<String, Object> claims = Map.of("id", memberId);

    byte[] base64EncodedSecret =
        Base64.getEncoder().encode(jwtProperties.getSecret().getBytes(StandardCharsets.UTF_8));
    SecretKey secretKey = Keys.hmacShaKeyFor(base64EncodedSecret);

    String bearerAccessToken = tokenProvider.generateBearerAccessToken(jti, email, claims);
    String accessToken = bearerAccessToken.substring("Bearer ".length());

    // when
    Claims result =
        Jwts.parser().verifyWith(secretKey).build().parseSignedClaims(accessToken).getPayload();

    // then
    Assertions.assertThat(result)
        .hasFieldOrPropertyWithValue("id", jti)
        .hasFieldOrPropertyWithValue("subject", email);
    Assertions.assertThat(result.get("id", Long.class)).isEqualTo(memberId);
  }

  @Test
  void generateRefreshToken() {
    // given
    String jti = "jti";
    String email = "test@email.com";

    String refreshToken = tokenProvider.generateRefreshToken(jti, email);

    byte[] base64EncodedSecret =
        Base64.getEncoder().encode(jwtProperties.getSecret().getBytes(StandardCharsets.UTF_8));
    SecretKey secretKey = Keys.hmacShaKeyFor(base64EncodedSecret);

    // when
    Claims result =
        Jwts.parser().verifyWith(secretKey).build().parseSignedClaims(refreshToken).getPayload();

    // then
    Assertions.assertThat(result)
        .hasFieldOrPropertyWithValue("id", jti)
        .hasFieldOrPropertyWithValue("subject", email);
  }

  @DisplayName("extractClaims(): Claims 추출 테스트")
  @Test
  void extractClaims() {
    // given
    JwtFactory jwtFactory = JwtFactory.withDefaultValues();
    String token = jwtFactory.generateToken(jwtProperties);

    // when
    Claims claims = tokenProvider.extractClaims(token);

    // then
    Assertions.assertThat(claims.getSubject()).isEqualTo(jwtFactory.getSubject());
    Assertions.assertThat(claims.getExpiration()).isBefore(jwtFactory.getExpiration());
    //  @Dis
  }

  @DisplayName("extractClaimsThrowSignatureException(): 유효하지 않은 토큰일때 SignautreException을 던진다.")
  @Test
  void extractClaimsThrowSignatureException() {
    // given
    String token = JwtFactory.withDefaultValues().generateInvalidToken();

    // when & then
    Assertions.assertThatThrownBy(() -> tokenProvider.extractClaims(token))
        .isInstanceOf(SignatureException.class);
  }

  @DisplayName("extractClaimsThrowExpiredJwtException(): 만료된 토큰일 때 ExpiredJwtException을 던진다.")
  @Test
  void extractClaimsThrowExpiredJwtException() {
    // given
    String token = JwtFactory.withExpired().generateToken(jwtProperties);

    // when & then
    Assertions.assertThatThrownBy(() -> tokenProvider.extractClaims(token))
        .isInstanceOf(ExpiredJwtException.class);
  }

  @DisplayName("refreshExpirationSeconds(): ")
  @Test
  void refreshExpirationSeconds() {
    // given
    int refreshTokenMinutes = 5;
    jwtProperties.setRefreshTokenExpirationMinutes(refreshTokenMinutes);

    // when
    Integer result = tokenProvider.refreshExpirationSeconds();

    // then
    Assertions.assertThat(result).isEqualTo(refreshTokenMinutes * 60);
  }
}
