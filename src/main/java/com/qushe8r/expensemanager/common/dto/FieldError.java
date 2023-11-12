package com.qushe8r.expensemanager.common.dto;

import java.util.List;
import org.springframework.validation.BindingResult;

public record FieldError(String field, String rejectedValue, String reason) {

  public static List<FieldError> of(BindingResult bindingResult) {
    return bindingResult.getFieldErrors().stream()
        .map(
            error ->
                new FieldError(
                    error.getField(),
                    error.getRejectedValue() == null ? "" : error.getRejectedValue().toString(),
                    error.getDefaultMessage()))
        .toList();
  }
}
