package com.qushe8r.expensemanager.security.utils;

import jakarta.servlet.http.Cookie;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import org.springframework.http.ResponseCookie;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class CookieCreator {

  public static final String REFRESH = "Refresh";

  public static final String REISSUE = "/reissue";

  public static Cookie createCookie(String refreshToken, String domain, int maxAge) {
    Cookie cookie = new Cookie(REFRESH, refreshToken);
    cookie.setDomain(domain);
    cookie.setPath(REISSUE);
    cookie.setMaxAge(maxAge);
    cookie.setHttpOnly(true);
    cookie.setSecure(true);
    return cookie;
  }

  public static ResponseCookie createResponseCookie(
      String refreshToken, String domain, long maxAge) {
    return ResponseCookie.from(REFRESH)
        .value(refreshToken)
        .domain(domain)
        .path(REISSUE)
        .maxAge(maxAge)
        .httpOnly(true)
        .secure(true)
        .build();
  }
}
