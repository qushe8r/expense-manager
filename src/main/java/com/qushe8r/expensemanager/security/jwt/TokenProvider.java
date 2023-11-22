package com.qushe8r.expensemanager.security.jwt;

import com.qushe8r.expensemanager.member.entity.Member;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import java.nio.charset.StandardCharsets;
import java.util.Base64;
import java.util.Calendar;
import java.util.Date;
import java.util.Map;
import javax.crypto.SecretKey;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class TokenProvider {

  private final JwtProperties jwtProperties;

  public static final String BEARER = "Bearer ";

  public String generateBearerAccessToken(String jti, Member member) {
    return BEARER
        + generateAccessToken(
            jti,
            member.getEmail(),
            new Date(),
            accessTokenExpiration(),
            claims(member),
            secretKey());
  }

  public String generateBearerAccessToken(String jti, String subject, Map<String, Object> claims) {
    return BEARER
        + generateAccessToken(
            jti, subject, new Date(), accessTokenExpiration(), claims, secretKey());
  }

  public String generateRefreshToken(String jti, String subject) {
    return generateRefreshToken(jti, subject, new Date(), refreshTokenExpiration(), secretKey());
  }

  public Claims extractClaims(String token) {
    return Jwts.parser().verifyWith(secretKey()).build().parseSignedClaims(token).getPayload();
  }

  public Integer refreshExpirationSeconds() {
    return jwtProperties.getRefreshTokenExpirationMinutes() * 60;
  }

  private String generateAccessToken(
      String jti,
      String subject,
      Date issuedAt,
      Date expiration,
      Map<String, Object> claims,
      SecretKey secretKey) {
    return Jwts.builder()
        .id(jti)
        .subject(subject)
        .issuedAt(issuedAt)
        .expiration(expiration)
        .claims(claims)
        .signWith(secretKey)
        .compact();
  }

  private String generateRefreshToken(
      String jti, String subject, Date issuedAt, Date expiration, SecretKey secretKey) {
    return Jwts.builder()
        .id(jti)
        .subject(subject)
        .issuedAt(issuedAt)
        .expiration(expiration)
        .signWith(secretKey)
        .compact();
  }

  private Map<String, Object> claims(Member member) {
    return Map.of("id", member.getId());
  }

  private SecretKey secretKey(String secret) {
    byte[] base64EncodedSecret =
        Base64.getEncoder().encode(secret.getBytes(StandardCharsets.UTF_8));
    return Keys.hmacShaKeyFor(base64EncodedSecret);
  }

  private SecretKey secretKey() {
    return secretKey(jwtProperties.getSecret());
  }

  private Date accessTokenExpiration() {
    return getTime(jwtProperties.getAccessTokenExpirationMinutes());
  }

  private Date refreshTokenExpiration() {
    return getTime(jwtProperties.getRefreshTokenExpirationMinutes());
  }

  private Date getTime(Integer minutes) {
    Calendar time = Calendar.getInstance();
    time.add(Calendar.MINUTE, minutes);
    return time.getTime();
  }
}
