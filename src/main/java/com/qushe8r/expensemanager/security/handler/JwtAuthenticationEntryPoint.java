package com.qushe8r.expensemanager.security.handler;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.qushe8r.expensemanager.common.dto.ErrorResponse;
import com.qushe8r.expensemanager.common.exception.ExceptionCode;
import com.qushe8r.expensemanager.security.exception.JwtExceptionCode;
import com.qushe8r.expensemanager.security.filter.JwtVerificationFilter;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class JwtAuthenticationEntryPoint implements AuthenticationEntryPoint {

  private final ObjectMapper objectMapper;

  @Override
  public void commence(
      HttpServletRequest request,
      HttpServletResponse response,
      AuthenticationException authException)
      throws IOException {
    JwtExceptionCode exceptionCode =
        (JwtExceptionCode) request.getAttribute(JwtVerificationFilter.EXCEPTION);
    if (exceptionCode == null) {
      exceptionCode = JwtExceptionCode.TOKEN_NOT_FOUND;
    }
    responseWithJson(response, exceptionCode);
  }

  private void responseWithJson(HttpServletResponse response, JwtExceptionCode exceptionCode)
      throws IOException {
    response.setContentType(MediaType.APPLICATION_JSON_VALUE);
    response.setCharacterEncoding(StandardCharsets.UTF_8.name());
    response.setStatus(exceptionCode.getStatus());
    response.getWriter().write(toJsonResponse(exceptionCode));
  }

  private String toJsonResponse(ExceptionCode exceptionCode) throws JsonProcessingException {
    ErrorResponse errorResponse = ErrorResponse.of(exceptionCode);
    return objectMapper.writeValueAsString(errorResponse);
  }
}
