package com.qushe8r.expensemanager.security.handler;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.qushe8r.expensemanager.security.jwt.TokenProvider;
import com.qushe8r.expensemanager.security.repository.RefreshTokenRepository;
import com.qushe8r.expensemanager.security.utils.CookieCreator;
import com.qushe8r.expensemanager.security.utils.CookieProperties;
import com.qushe8r.expensemanager.stub.JwtFactory;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.BDDMockito;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpHeaders;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.security.authentication.TestingAuthenticationToken;
import org.springframework.security.core.Authentication;

@ExtendWith(MockitoExtension.class)
class JwtLogoutHandlerTest {

  @Spy private CookieProperties cookieProperties;

  @Spy private ObjectMapper objectMapper;

  @Mock private TokenProvider tokenProvider;

  @Mock private RefreshTokenRepository refreshTokenRepository;

  @InjectMocks private JwtLogoutHandler jwtLogoutHandler;

  @Test
  void onLogoutSuccess() {
    // given
    String token = JwtFactory.withDefaultValues().generateStubToken();

    MockHttpServletRequest request = new MockHttpServletRequest();
    MockHttpServletResponse response = new MockHttpServletResponse();
    Authentication authentication = new TestingAuthenticationToken(null, null, "authorities");

    request.addHeader(HttpHeaders.AUTHORIZATION, TokenProvider.BEARER + token);
    BDDMockito.given(tokenProvider.extractClaims(token))
        .willReturn(JwtFactory.withDefaultValues().getStubClaims());
    BDDMockito.doNothing().when(refreshTokenRepository).deleteById("jti");

    jwtLogoutHandler.onLogoutSuccess(request, response, authentication);

    Assertions.assertThat(response.getCookie(CookieCreator.REFRESH))
        .hasFieldOrPropertyWithValue("value", null)
        .hasFieldOrPropertyWithValue("Domain", null)
        .hasFieldOrPropertyWithValue("maxAge", 0)
        .hasFieldOrPropertyWithValue("HttpOnly", true)
        .hasFieldOrPropertyWithValue("Secure", true)
        .hasFieldOrPropertyWithValue("Path", "/reissue");
  }
}
