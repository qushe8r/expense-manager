package com.qushe8r.expensemanager.common.dto;

import java.util.List;
import org.springframework.data.domain.Page;

public record MultiResponse<T>(List<T> data, PageInfo pageInfo) {

  public MultiResponse(List<T> data, Page<?> page) {
    this(
        data,
        new PageInfo(
            page.getNumber() + 1, page.getSize(), page.getTotalElements(), page.getTotalPages()));
  }

  public MultiResponse(Page<T> page) {
    this(
        page.getContent(),
        new PageInfo(
            page.getNumber() + 1, page.getSize(), page.getTotalElements(), page.getTotalPages()));
  }
}
