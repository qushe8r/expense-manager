package com.qushe8r.expensemanager.category.service;

import com.qushe8r.expensemanager.category.dto.PostCategory;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Transactional(readOnly = true)
@Service
@RequiredArgsConstructor
public class CategoryService {

  public Long crateCategory(PostCategory dto) {
    return null;
  }
}
