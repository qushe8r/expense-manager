package com.qushe8r.expensemanager.security.utils;

import static org.junit.jupiter.api.Assertions.*;

import jakarta.servlet.http.Cookie;
import java.time.Duration;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.http.ResponseCookie;

class CookieCreatorTest {

  @Test
  void createCookie() {
    // given
    String refreshToken = "refreshToken";
    String domain = "domain";
    int maxAge = 3000;

    // when
    Cookie result = CookieCreator.createCookie(refreshToken, domain, maxAge);

    // then
    Assertions.assertThat(result)
        .hasFieldOrPropertyWithValue("value", refreshToken)
        .hasFieldOrPropertyWithValue("domain", domain)
        .hasFieldOrPropertyWithValue("maxAge", maxAge)
        .hasFieldOrPropertyWithValue("httpOnly", true)
        .hasFieldOrPropertyWithValue("secure", true);
  }

  @Test
  void createResponseCookie() {
    // given
    String refreshToken = "refreshToken";
    String domain = "domain";
    int maxAge = 3000;

    // when
    ResponseCookie result = CookieCreator.createResponseCookie(refreshToken, domain, maxAge);

    Assertions.assertThat(result)
        .hasFieldOrPropertyWithValue("value", refreshToken)
        .hasFieldOrPropertyWithValue("domain", domain)
        .hasFieldOrPropertyWithValue("maxAge", Duration.ofSeconds(maxAge))
        .hasFieldOrPropertyWithValue("httpOnly", true)
        .hasFieldOrPropertyWithValue("secure", true);
  }
}
