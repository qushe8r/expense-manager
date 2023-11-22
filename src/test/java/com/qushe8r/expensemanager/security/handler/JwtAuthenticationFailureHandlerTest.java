package com.qushe8r.expensemanager.security.handler;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.qushe8r.expensemanager.common.dto.ErrorResponse;
import com.qushe8r.expensemanager.security.exception.AuthExceptionCode;
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
class JwtAuthenticationFailureHandlerTest {

  @Spy private ObjectMapper objectMapper;

  @InjectMocks private JwtAuthenticationFailureHandler jwtAuthenticationFailureHandler;

  @DisplayName("onAuthenticationFailure(): 로그인 실패하면 정보 불일치 응답을 준다.")
  @Test
  void onAuthenticationFailure() throws IOException {
    // given
    MockHttpServletRequest request = new MockHttpServletRequest();
    MockHttpServletResponse response = new MockHttpServletResponse();
    UsernameNotFoundException exception = new UsernameNotFoundException("msg");

    // when
    jwtAuthenticationFailureHandler.onAuthenticationFailure(request, response, exception);

    // then
    String actual = response.getContentAsString();
    String expected =
        objectMapper.writeValueAsString(
            ErrorResponse.of(AuthExceptionCode.INFORMATION_NOT_MATCHED));

    Assertions.assertThat(actual).isEqualTo(expected);
  }
}
