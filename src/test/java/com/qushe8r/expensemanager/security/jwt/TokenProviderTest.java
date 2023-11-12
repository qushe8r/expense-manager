package com.qushe8r.expensemanager.security.jwt;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import java.nio.charset.StandardCharsets;
import java.time.Duration;
import java.util.Base64;
import java.util.Date;
import java.util.Map;
import java.util.Random;
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

  @DisplayName("generateAccessToken(): JWT 정상 발급 테스트")
  @Test
  void generateAccessToken() {
    // given
    String subject = "subject";

    String secret = "WITHOUT_WEAK_KEY_EXCEPTION";
    byte[] base64EncodedSecret =
        Base64.getEncoder().encode(secret.getBytes(StandardCharsets.UTF_8));
    SecretKey secretKey = Keys.hmacShaKeyFor(base64EncodedSecret);

    String stringName = "stringName";
    String stringValue = "string";
    String longName = "longName";
    Long longValue = new Random().nextLong();
    Map<String, Object> claims = Map.of(stringName, stringValue, longName, longValue);

    String accessToken =
        tokenProvider.generateAccessToken(
            subject,
            new Date(),
            new Date(new Date().getTime() + Duration.ofMinutes(5).toMillis()),
            claims,
            secret);

    // when
    Claims payload =
        Jwts.parser().verifyWith(secretKey).build().parseSignedClaims(accessToken).getPayload();

    // then
    Assertions.assertThat(payload.getSubject()).isEqualTo("subject");
    Assertions.assertThat(payload.get(stringName, String.class)).isEqualTo(stringValue);
    Assertions.assertThat(payload.get(longName, Long.class)).isEqualTo(longValue);
  }
}
