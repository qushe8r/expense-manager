package com.qushe8r.expensemanager.security.filter;

import com.qushe8r.expensemanager.member.entity.MemberDetails;
import com.qushe8r.expensemanager.security.exception.JwtExceptionCode;
import com.qushe8r.expensemanager.security.jwt.TokenProvider;
import com.qushe8r.expensemanager.stub.JwtFactory;
import com.qushe8r.expensemanager.stub.StubClaims;
import com.qushe8r.expensemanager.stub.StubHeader;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.security.SignatureException;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.BDDMockito;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpHeaders;
import org.springframework.mock.web.MockFilterChain;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;

@ExtendWith(MockitoExtension.class)
class JwtVerificationFilterTest {

  @Mock private TokenProvider tokenProvider;

  @InjectMocks private JwtVerificationFilter filter;

  @DisplayName("shouldNotFilterFalse(): 조건에 만족하면 false")
  @Test
  void shouldNotFilterFalse() {
    // given
    MockHttpServletRequest request = new MockHttpServletRequest();
    request.addHeader(HttpHeaders.AUTHORIZATION, "Bearer ACCESS_TOKEN");

    // when
    boolean result = filter.shouldNotFilter(request);

    // then
    Assertions.assertThat(result).isFalse();
  }

  @DisplayName("shouldNotFilterBearerNull(): Bearer == null 이면 true")
  @Test
  void shouldNotFilterBearerNull() {
    MockHttpServletRequest request = new MockHttpServletRequest();

    // when
    boolean result = filter.shouldNotFilter(request);

    // then
    Assertions.assertThat(result).isTrue();
  }

  @DisplayName("shouldNotFilterBearerNull(): Bearer로 시작하지 않으면 true")
  @Test
  void shouldNotFilterStartWithFalse() {
    MockHttpServletRequest request = new MockHttpServletRequest();
    request.addHeader(HttpHeaders.AUTHORIZATION, "ACCESS_TOKEN");

    // when
    boolean result = filter.shouldNotFilter(request);

    // then
    Assertions.assertThat(result).isTrue();
  }

  @DisplayName("doFilterInternal(): 인증에 성공하면 MemberDetails가 SecurityContextHolder에 담긴다.")
  @Test
  void doFilterInternal() {
    MockHttpServletRequest request = new MockHttpServletRequest();
    MockHttpServletResponse response = new MockHttpServletResponse();
    MockFilterChain filterChain = new MockFilterChain();

    String token = JwtFactory.withDefaultValues().generateStubToken();
    request.addHeader(HttpHeaders.AUTHORIZATION, TokenProvider.BEARER + token);

    BDDMockito.given(tokenProvider.extractClaims(token))
        .willReturn(JwtFactory.withDefaultValues().getStubClaims());

    filter.doFilterInternal(request, response, filterChain);

    Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
    Assertions.assertThat(authentication.getPrincipal()).isInstanceOf(MemberDetails.class);
  }

  @DisplayName(
      "doFilterInternalExpiredJwtException(): 만료시 request attribute에 ExceptionCode가 담겨 있다.")
  @Test
  void doFilterInternalExpiredJwtException() {
    MockHttpServletRequest request = new MockHttpServletRequest();
    MockHttpServletResponse response = new MockHttpServletResponse();
    MockFilterChain filterChain = new MockFilterChain();

    String token = JwtFactory.withExpired().generateStubToken();
    request.addHeader(HttpHeaders.AUTHORIZATION, TokenProvider.BEARER + token);

    BDDMockito.given(tokenProvider.extractClaims(token))
        .willThrow(new ExpiredJwtException(new StubHeader(), new StubClaims(), "msg"));

    filter.doFilterInternal(request, response, filterChain);

    Assertions.assertThat(request.getAttribute(JwtVerificationFilter.EXCEPTION))
        .isInstanceOf(JwtExceptionCode.class)
        .hasFieldOrPropertyWithValue("errorCode", "JX01")
        .hasFieldOrPropertyWithValue("status", 401)
        .hasFieldOrPropertyWithValue("message", "액세스 토큰이 만료 되었습니다.");
  }

  @DisplayName(
      "doFilterInternalSignatureException(): 에러 발생시 request attribute에 ExceptionCode가 담겨 있다.")
  @Test
  void doFilterInternalSignatureException() {
    MockHttpServletRequest request = new MockHttpServletRequest();
    MockHttpServletResponse response = new MockHttpServletResponse();
    MockFilterChain filterChain = new MockFilterChain();

    String token = JwtFactory.withExpired().generateStubToken();
    request.addHeader(HttpHeaders.AUTHORIZATION, TokenProvider.BEARER + token);

    BDDMockito.given(tokenProvider.extractClaims(token)).willThrow(new SignatureException("msg"));

    filter.doFilterInternal(request, response, filterChain);

    Assertions.assertThat(request.getAttribute(JwtVerificationFilter.EXCEPTION))
        .isInstanceOf(JwtExceptionCode.class)
        .hasFieldOrPropertyWithValue("errorCode", "JX02")
        .hasFieldOrPropertyWithValue("status", 401)
        .hasFieldOrPropertyWithValue("message", "유효하지 않은 토큰입니다.");
  }

  @DisplayName("doFilterInternalJwtException(): 에러 발생시 request attribute에 ExceptionCode가 담겨 있다.")
  @Test
  void doFilterInternalJwtException() {
    MockHttpServletRequest request = new MockHttpServletRequest();
    MockHttpServletResponse response = new MockHttpServletResponse();
    MockFilterChain filterChain = new MockFilterChain();

    String token = JwtFactory.withExpired().generateStubToken();
    request.addHeader(HttpHeaders.AUTHORIZATION, TokenProvider.BEARER + token);

    BDDMockito.given(tokenProvider.extractClaims(token)).willThrow(new JwtException("msg"));

    filter.doFilterInternal(request, response, filterChain);

    Assertions.assertThat(request.getAttribute(JwtVerificationFilter.EXCEPTION))
        .isInstanceOf(JwtExceptionCode.class)
        .hasFieldOrPropertyWithValue("errorCode", "JX02")
        .hasFieldOrPropertyWithValue("status", 401)
        .hasFieldOrPropertyWithValue("message", "유효하지 않은 토큰입니다.");
  }
}
