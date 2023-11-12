package com.qushe8r.expensemanager.security.handler;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.qushe8r.expensemanager.common.dto.ErrorResponse;
import com.qushe8r.expensemanager.common.exception.ExceptionCode;
import com.qushe8r.expensemanager.security.exception.AuthExceptionCode;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class JwtAuthenticationFailureHandler implements AuthenticationFailureHandler {

  private final ObjectMapper objectMapper;

  @Override
  public void onAuthenticationFailure(
      HttpServletRequest request, HttpServletResponse response, AuthenticationException exception)
      throws IOException {
    ExceptionCode exceptionCode = AuthExceptionCode.INFORMATION_NOT_MATCHED;
    ErrorResponse errorResponse = ErrorResponse.of(exceptionCode);
    response.setContentType(MediaType.APPLICATION_JSON_VALUE);
    response.setCharacterEncoding(StandardCharsets.UTF_8.name());
    response.setStatus(exceptionCode.getStatus());
    response.getWriter().write(objectMapper.writeValueAsString(errorResponse));
  }
}
