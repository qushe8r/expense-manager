package com.qushe8r.expensemanager.security.controller;

import com.qushe8r.expensemanager.security.service.AuthService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CookieValue;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class AuthController {

  private final AuthService authService;

  @PostMapping("/reissue")
  public ResponseEntity<Void> reissue(@CookieValue("Refresh") String refreshToken) {
    HttpHeaders headers = authService.reissue(refreshToken);
    return ResponseEntity.ok().headers(headers).build();
  }
}
