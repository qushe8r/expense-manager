package com.qushe8r.expensemanager.common.utils;

import com.qushe8r.expensemanager.common.exception.ValidateDurationException;
import com.qushe8r.expensemanager.common.exception.ValidateEndBeforeStartException;
import com.qushe8r.expensemanager.common.exception.ValidateRangeException;
import java.time.LocalDate;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class Validator {
  public static void validateDate(LocalDate start, LocalDate end) {
    if (end.isBefore(start)) {
      throw new ValidateEndBeforeStartException();
    }
    if (start.plusDays(31L).isBefore(end)) {
      throw new ValidateDurationException();
    }
  }

  public static void validateAmount(Long min, Long max) {
    if (min == null || max == null) {
      return;
    }
    if (min > max) {
      throw new ValidateRangeException();
    }
  }
}
