package com.qushe8r.expensemanager.security.handler;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.qushe8r.expensemanager.common.dto.ErrorResponse;
import com.qushe8r.expensemanager.security.exception.JwtExceptionCode;
import com.qushe8r.expensemanager.security.filter.JwtVerificationFilter;
import java.io.IOException;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

@ExtendWith(MockitoExtension.class)
class JwtAuthenticationEntryPointTest {

  @Spy private ObjectMapper objectMapper;

  @InjectMocks private JwtAuthenticationEntryPoint jwtAuthenticationEntryPoint;

  @DisplayName("commenceInvalidToken(): INVALID_TOKEN이 담겨 있을때 응답")
  @Test
  void commenceInvalidToken() throws IOException {
    // given
    MockHttpServletRequest request = new MockHttpServletRequest();
    MockHttpServletResponse response = new MockHttpServletResponse();
    UsernameNotFoundException authException = new UsernameNotFoundException("msg");

    request.setAttribute(JwtVerificationFilter.EXCEPTION, JwtExceptionCode.INVALID_TOKEN);

    // when
    jwtAuthenticationEntryPoint.commence(request, response, authException);

    // then
    String actual = response.getContentAsString();
    String expected =
        objectMapper.writeValueAsString(ErrorResponse.of(JwtExceptionCode.INVALID_TOKEN));

    Assertions.assertThat(actual).isEqualTo(expected);
  }

  @DisplayName("commenceAccessTokenExpired(): ACCESS_TOKEN_EXPIRED가 담겨 있을때 응답")
  @Test
  void commenceAccessTokenExpired() throws IOException {
    // given
    MockHttpServletRequest request = new MockHttpServletRequest();
    MockHttpServletResponse response = new MockHttpServletResponse();
    UsernameNotFoundException authException = new UsernameNotFoundException("msg");

    request.setAttribute(JwtVerificationFilter.EXCEPTION, JwtExceptionCode.ACCESS_TOKEN_EXPIRED);

    // when
    jwtAuthenticationEntryPoint.commence(request, response, authException);

    // then
    String actual = response.getContentAsString();
    String expected =
        objectMapper.writeValueAsString(ErrorResponse.of(JwtExceptionCode.ACCESS_TOKEN_EXPIRED));

    Assertions.assertThat(actual).isEqualTo(expected);
  }
}
