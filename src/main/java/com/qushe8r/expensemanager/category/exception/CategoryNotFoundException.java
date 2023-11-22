package com.qushe8r.expensemanager.category.exception;

import com.qushe8r.expensemanager.common.exception.GlobalException;

public class CategoryNotFoundException extends GlobalException {

  public CategoryNotFoundException() {
    super(CategoryExceptionCode.CATEGORY_NOT_FOUND);
  }
}
