package com.qushe8r.expensemanager.common.dto;

import java.util.List;
import java.util.Set;

public record ConstraintViolation(String propertyPath, String rejectedValue, String reason) {

  public static List<ConstraintViolation> of(
      Set<jakarta.validation.ConstraintViolation<?>> constraintViolations) {
    return constraintViolations.stream()
        .map(
            constraintViolation ->
                new ConstraintViolation(
                    constraintViolation.getPropertyPath().toString(),
                    constraintViolation.getInvalidValue().toString(),
                    constraintViolation.getMessage()))
        .toList();
  }
}
