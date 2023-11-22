package com.qushe8r.expensemanager.common.dto;

import com.qushe8r.expensemanager.expense.dto.ExpenseResponse;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;

class MultiResponseTest {

  @DisplayName("page(): Page를 한번에 조회 변환하여 입력했을 때 정상적으로 동작한다")
  @Test
  void page() {
    ExpenseResponse expenseResponse1 =
        new ExpenseResponse(1L, 10000L, "김밥", LocalDateTime.of(2023, 11, 15, 12, 0), "식비");
    ExpenseResponse expenseResponse2 =
        new ExpenseResponse(2L, 10000L, "김밥", LocalDateTime.of(2023, 11, 16, 12, 0), "식비");
    ExpenseResponse expenseResponse3 =
        new ExpenseResponse(3L, 10000L, "김밥", LocalDateTime.of(2023, 11, 17, 12, 0), "식비");

    PageImpl<ExpenseResponse> expenseResponses =
        new PageImpl<>(
            List.of(expenseResponse1, expenseResponse2, expenseResponse3),
            PageRequest.of(0, 3),
            100);

    MultiResponse<ExpenseResponse> result = new MultiResponse<>(expenseResponses);

    Assertions.assertThat(result.data()).size().isEqualTo(3);
    Assertions.assertThat(result.data().get(0)).isEqualTo(expenseResponse1);
    Assertions.assertThat(result.data().get(1)).isEqualTo(expenseResponse2);
    Assertions.assertThat(result.data().get(2)).isEqualTo(expenseResponse3);
    Assertions.assertThat(result.pageInfo())
        .hasFieldOrPropertyWithValue("page", 1)
        .hasFieldOrPropertyWithValue("size", 3)
        .hasFieldOrPropertyWithValue("totalElements", 100L)
        .hasFieldOrPropertyWithValue("totalPages", 34);
  }

  @DisplayName("data(): 페이지정보와 리스트를 따로 조회했을때 따로 입력하여도 정상적으로 동작한다")
  @Test
  void data() {
    ExpenseResponse expenseResponse1 =
        new ExpenseResponse(1L, 10000L, "김밥", LocalDateTime.of(2023, 11, 15, 12, 0), "식비");
    ExpenseResponse expenseResponse2 =
        new ExpenseResponse(2L, 10000L, "김밥", LocalDateTime.of(2023, 11, 16, 12, 0), "식비");
    ExpenseResponse expenseResponse3 =
        new ExpenseResponse(3L, 10000L, "김밥", LocalDateTime.of(2023, 11, 17, 12, 0), "식비");

    PageImpl<ExpenseResponse> expenseResponses =
        new PageImpl<>(new ArrayList<>(), PageRequest.of(0, 3), 100);

    MultiResponse<ExpenseResponse> result =
        new MultiResponse<>(
            List.of(expenseResponse1, expenseResponse2, expenseResponse3), expenseResponses);

    Assertions.assertThat(result.data()).size().isEqualTo(3);
    Assertions.assertThat(result.data().get(0)).isEqualTo(expenseResponse1);
    Assertions.assertThat(result.data().get(1)).isEqualTo(expenseResponse2);
    Assertions.assertThat(result.data().get(2)).isEqualTo(expenseResponse3);
    Assertions.assertThat(result.pageInfo())
        .hasFieldOrPropertyWithValue("page", 1)
        .hasFieldOrPropertyWithValue("size", 3)
        .hasFieldOrPropertyWithValue("totalElements", 100L)
        .hasFieldOrPropertyWithValue("totalPages", 34);
  }
}
