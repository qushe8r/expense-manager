package com.qushe8r.expensemanager.common.utils;

import static org.junit.jupiter.api.Assertions.*;

import com.qushe8r.expensemanager.common.exception.ValidateDurationException;
import com.qushe8r.expensemanager.common.exception.ValidateEndBeforeStartException;
import com.qushe8r.expensemanager.common.exception.ValidateRangeException;
import java.time.LocalDate;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;

class ValidatorTest {

  @Test
  void validateDate() {
    LocalDate start = LocalDate.of(2023, 11, 1);
    LocalDate end = LocalDate.of(2023, 11, 30);
    Validator.validateDate(start, end);
  }

  @Test
  void validateDateValidateEndBeforeStartException() {
    LocalDate end = LocalDate.of(2023, 11, 1);
    LocalDate start = LocalDate.of(2023, 11, 30);
    Assertions.assertThatThrownBy(() -> Validator.validateDate(start, end))
        .isInstanceOf(ValidateEndBeforeStartException.class);
  }

  @Test
  void validateDateValidateDurationException() {
    LocalDate start = LocalDate.of(2023, 11, 1);
    LocalDate end = LocalDate.of(2023, 12, 3);
    Assertions.assertThatThrownBy(() -> Validator.validateDate(start, end))
        .isInstanceOf(ValidateDurationException.class);
  }

  @Test
  void validateAmount() {
    Long min = 1000L;
    Long max = 10000L;
    Validator.validateAmount(min, max);
  }

  @Test
  void validateAmountMinNull() {
    Long min = null;
    Long max = 10000L;
    Validator.validateAmount(min, max);
  }

  @Test
  void validateAmountMaxNull() {
    Long min = 1000L;
    Long max = null;
    Validator.validateAmount(min, max);
  }

  @Test
  void validateAmountMinMaxNull() {
    Long min = null;
    Long max = null;
    Validator.validateAmount(min, max);
  }

  @Test
  void validateAmountValidateRangeException() {
    Long min = 10000L;
    Long max = 1000L;
    Assertions.assertThatThrownBy(() -> Validator.validateAmount(min, max))
        .isInstanceOf(ValidateRangeException.class);
  }
}
