package com.qushe8r.expensemanager.security.filter;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.qushe8r.expensemanager.macher.UsernamePasswordAuthenticationTokenMatcher;
import com.qushe8r.expensemanager.member.entity.MemberDetails;
import com.qushe8r.expensemanager.security.dto.UsernamePassword;
import com.qushe8r.expensemanager.security.jwt.JwtProperties;
import com.qushe8r.expensemanager.security.jwt.TokenProvider;
import com.qushe8r.expensemanager.security.repository.RefreshTokenRepository;
import com.qushe8r.expensemanager.security.utils.CookieCreator;
import com.qushe8r.expensemanager.security.utils.CookieProperties;
import com.qushe8r.expensemanager.stub.JwtFactory;
import java.io.IOException;
import java.util.List;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.BDDMockito;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpHeaders;
import org.springframework.mock.web.MockFilterChain;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.authority.SimpleGrantedAuthority;

@ExtendWith(MockitoExtension.class)
class JwtAuthenticationFilterTest {

  @Spy private JwtProperties jwtProperties;

  @Mock private TokenProvider tokenProvider;

  @Spy private CookieProperties cookieProperties;

  @Spy private ObjectMapper objectMapper;

  @Mock private RefreshTokenRepository refreshTokenRepository;

  @Mock private AuthenticationManager authenticationManager;

  @InjectMocks private JwtAuthenticationFilter filter;

  @DisplayName("attemptAuthentication(): 인증 성공")
  @Test
  void attemptAuthentication() throws IOException {
    // given
    MockHttpServletRequest req = new MockHttpServletRequest();
    MockHttpServletResponse res = new MockHttpServletResponse();

    UsernamePassword usernamePassword = new UsernamePassword("username", "password");
    String content = objectMapper.writeValueAsString(usernamePassword);
    req.setContent(content.getBytes());

    filter.setAuthenticationManager(authenticationManager);
    BDDMockito.given(
            authenticationManager.authenticate(
                Mockito.argThat(
                    new UsernamePasswordAuthenticationTokenMatcher(
                        new UsernamePasswordAuthenticationToken("username", "password")))))
        .willReturn(
            new UsernamePasswordAuthenticationToken(
                "인증된", "", List.of(new SimpleGrantedAuthority("ROLE_USER"))));

    // when
    Authentication authentication = filter.attemptAuthentication(req, res);

    // then
    Assertions.assertThat(authentication.isAuthenticated()).isTrue();
  }

  @DisplayName("attemptAuthentication(): 인증 성공")
  @Test
  void attemptAuthenticationError() throws IOException {
    // given
    MockHttpServletRequest req = new MockHttpServletRequest();
    MockHttpServletResponse res = new MockHttpServletResponse();

    UsernamePassword usernamePassword = new UsernamePassword("username", "wrong");
    String content = objectMapper.writeValueAsString(usernamePassword);
    req.setContent(content.getBytes());

    filter.setAuthenticationManager(authenticationManager);

    BDDMockito.given(
            authenticationManager.authenticate(
                Mockito.argThat(
                    new UsernamePasswordAuthenticationTokenMatcher(
                        new UsernamePasswordAuthenticationToken("username", "wrong")))))
        .willThrow(new BadCredentialsException("msg"));

    // when & then
    Assertions.assertThatThrownBy(() -> filter.attemptAuthentication(req, res))
        .isInstanceOf(AuthenticationException.class);
    Mockito.verify(authenticationManager, Mockito.times(1))
        .authenticate(Mockito.any(Authentication.class));
  }

  @DisplayName("successfulAuthentication(): 인증 성공하면 header와 cookie에 토큰을 받는다.")
  @Test
  void successfulAuthentication() {
    // given
    jwtProperties.setSecret(JwtFactory.TEST_SECRET);
    jwtProperties.setAccessTokenExpirationMinutes(100);
    jwtProperties.setRefreshTokenExpirationMinutes(100);

    cookieProperties.setDomain("DOMAIN");

    MemberDetails memberDetails = new MemberDetails(1L, "username", "password");
    UsernamePasswordAuthenticationToken usernamePasswordAuthenticationToken =
        new UsernamePasswordAuthenticationToken(memberDetails, "", memberDetails.getAuthorities());

    MockHttpServletRequest request = new MockHttpServletRequest();
    MockHttpServletResponse response = new MockHttpServletResponse();
    MockFilterChain filterChain = new MockFilterChain();

    BDDMockito.given(
            tokenProvider.generateBearerAccessToken(
                Mockito.anyString(), Mockito.eq("username"), Mockito.anyMap()))
        .willReturn("ACCESS_TOKEN");
    BDDMockito.given(
            tokenProvider.generateRefreshToken(Mockito.anyString(), Mockito.eq("username")))
        .willReturn("REFRESH_TOKEN");

    // when
    filter.successfulAuthentication(
        request, response, filterChain, usernamePasswordAuthenticationToken);

    // then
    Assertions.assertThat(response.getHeader(HttpHeaders.AUTHORIZATION)).isEqualTo("ACCESS_TOKEN");
    Assertions.assertThat(response.getCookie(CookieCreator.REFRESH))
        .hasFieldOrPropertyWithValue("value", "REFRESH_TOKEN")
        .hasFieldOrPropertyWithValue("Domain", "domain")
        .hasFieldOrPropertyWithValue("maxAge", 6000)
        .hasFieldOrPropertyWithValue("HttpOnly", true)
        .hasFieldOrPropertyWithValue("Secure", true)
        .hasFieldOrPropertyWithValue("Path", "/reissue");
  }
}
