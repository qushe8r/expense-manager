package com.qushe8r.expensemanager.common.dto;

import com.qushe8r.expensemanager.common.exception.ExceptionCode;
import java.util.List;
import java.util.Set;
import org.springframework.http.HttpStatus;
import org.springframework.validation.BindingResult;

public record ErrorResponse(
    String errorCode,
    Integer status,
    String message,
    List<FieldError> fieldErrors,
    List<ConstraintViolation> violationErrors) {

  private ErrorResponse(String errorCode, int status, String message) {
    this(errorCode, status, message, null, null);
  }

  private ErrorResponse(List<FieldError> fieldErrors, List<ConstraintViolation> violationErrors) {
    this(null, null, null, fieldErrors, violationErrors);
  }

  public static ErrorResponse of(BindingResult bindingResult) {
    return new ErrorResponse(FieldError.of(bindingResult), null);
  }

  public static ErrorResponse of(Set<jakarta.validation.ConstraintViolation<?>> violations) {
    return new ErrorResponse(null, ConstraintViolation.of(violations));
  }

  public static ErrorResponse of(ExceptionCode exceptionCode) {
    return new ErrorResponse(
        exceptionCode.getErrorCode(), exceptionCode.getStatus(), exceptionCode.getMessage());
  }

  public static ErrorResponse of(HttpStatus httpStatus) {
    return new ErrorResponse(null, httpStatus.value(), httpStatus.getReasonPhrase());
  }

  public static ErrorResponse of(HttpStatus httpStatus, String message) {
    return new ErrorResponse(null, httpStatus.value(), message);
  }
}
