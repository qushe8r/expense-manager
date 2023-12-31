package com.qushe8r.expensemanager.stub;

import com.qushe8r.expensemanager.security.jwt.JwtProperties;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import java.nio.charset.StandardCharsets;
import java.time.Duration;
import java.util.Base64;
import java.util.Calendar;
import java.util.Date;
import java.util.Map;
import javax.crypto.SecretKey;

public class JwtFactory {

  public static final String TEST_SECRET = "JWT_SECRET_KEY_FOR_TEST_WITHOUT_WEAK_KEY_EXCEPTION";

  private String jti = "jti";

  private String subject = "test@email.com";

  private Date issuedAt = new Date();

  private Date expiration = new Date(new Date().getTime() + Duration.ofMinutes(10).toMillis());

  private Map<String, Object> claims = Map.of("id", 1L);

  public JwtFactory(String subject, Date issuedAt, Date expiration, Map<String, Object> claims) {
    this.subject = subject != null ? subject : this.subject;
    this.issuedAt = issuedAt != null ? issuedAt : this.issuedAt;
    this.expiration = expiration != null ? expiration : this.expiration;
    this.claims = claims != null ? claims : this.claims;
  }

  public static JwtFactory withDefaultValues() {
    return new JwtFactory(null, null, null, null);
  }

  public static JwtFactory withAnotherUserValues() {
    return new JwtFactory("anotherUser@email.com", null, null, Map.of("id", 2L));
  }

  public static JwtFactory withExpired() {
    return new JwtFactory(null, generateTime(-10), generateTime(0), null);
  }

  private static Date generateTime(Integer minutes) {
    Calendar date = Calendar.getInstance();
    date.add(Calendar.MINUTE, minutes);
    return date.getTime();
  }

  public String generateToken(JwtProperties jwtProperties) {
    return Jwts.builder()
        .id(jti)
        .subject(subject)
        .issuedAt(issuedAt)
        .expiration(expiration)
        .claims(claims)
        .signWith(secretKey(jwtProperties))
        .compact();
  }

  public String generateStubToken() {
    return Jwts.builder()
        .id(jti)
        .subject(subject)
        .issuedAt(issuedAt)
        .expiration(expiration)
        .claims(claims)
        .signWith(secretKey(TEST_SECRET))
        .compact();
  }

  public String generateInvalidToken() {
    return Jwts.builder()
        .id(jti)
        .subject(subject)
        .issuedAt(issuedAt)
        .expiration(expiration)
        .claims(claims)
        .signWith(secretKey("INVALID_VALUE_SECRET_KEY_FOR_INVALID_TOKEN_TEST"))
        .compact();
  }

  public Claims getStubClaims() {
    return Jwts.parser()
        .verifyWith(secretKey(TEST_SECRET))
        .build()
        .parseSignedClaims(generateStubToken())
        .getPayload();
  }

  private SecretKey secretKey(String secret) {
    byte[] base64EncodedSecret =
        Base64.getEncoder().encode(secret.getBytes(StandardCharsets.UTF_8));
    return Keys.hmacShaKeyFor(base64EncodedSecret);
  }

  private SecretKey secretKey(JwtProperties jwtProperties) {
    String secret = jwtProperties.getSecret();
    return secretKey(secret);
  }

  public String getSubject() {
    return subject;
  }

  public Date getIssuedAt() {
    return issuedAt;
  }

  public Date getExpiration() {
    return expiration;
  }

  public Map<String, Object> getClaims() {
    return claims;
  }
}
