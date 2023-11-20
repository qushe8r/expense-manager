package com.qushe8r.expensemanager.common.converter;

import java.sql.Date;
import java.time.YearMonth;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;

class YearMonthAttributeConverterTest {

  YearMonthAttributeConverter yearMonthAttributeConverter = new YearMonthAttributeConverter();

  @Test
  void convertToEntityAttribute() {
    // given
    Date date = Date.valueOf("2023-11-15");

    // when
    YearMonth yearMonth = yearMonthAttributeConverter.convertToEntityAttribute(date);

    // then
    Assertions.assertThat(yearMonth).isEqualTo(YearMonth.of(2023, 11));
  }

  @Test
  void convertToEntityAttributeNull() {
    // given
    Date date = null;

    // when
    YearMonth yearMonth = yearMonthAttributeConverter.convertToEntityAttribute(date);

    // then
    Assertions.assertThat(yearMonth).isNull();
  }

  @Test
  void convertToDatabaseColumn() {
    // given
    YearMonth month = YearMonth.of(2023, 11);

    // when
    Date date = yearMonthAttributeConverter.convertToDatabaseColumn(month);

    // then
    Assertions.assertThat(date).isEqualTo("2023-11-1");
  }

  @Test
  void convertToDatabaseColumnNull() {
    // given
    YearMonth month = null;

    // when
    Date date = yearMonthAttributeConverter.convertToDatabaseColumn(month);

    // then
    Assertions.assertThat(date).isNull();
  }
}
