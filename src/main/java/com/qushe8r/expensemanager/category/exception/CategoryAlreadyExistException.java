package com.qushe8r.expensemanager.category.exception;

import com.qushe8r.expensemanager.common.exception.GlobalException;

public class CategoryAlreadyExistException extends GlobalException {

  public CategoryAlreadyExistException() {
    super(CategoryExceptionCode.CATEGORY_ALREADY_EXIST);
  }
}
