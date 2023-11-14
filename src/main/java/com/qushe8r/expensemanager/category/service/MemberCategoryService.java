package com.qushe8r.expensemanager.category.service;

import com.qushe8r.expensemanager.category.entity.Category;
import com.qushe8r.expensemanager.category.entity.MemberCategory;
import com.qushe8r.expensemanager.category.repository.MemberCategoryRepository;
import com.qushe8r.expensemanager.member.entity.Member;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Transactional(readOnly = true)
@Service
@RequiredArgsConstructor
public class MemberCategoryService {

  private final MemberCategoryRepository memberCategoryRepository;

  @Transactional
  public MemberCategory findByMemberCategoryOrElseSave(Long memberId, Long categoryId) {
    return memberCategoryRepository
        .findByMemberIdAndCategoryId(memberId, categoryId)
        .orElseGet(
            () ->
                memberCategoryRepository.save(
                    new MemberCategory(new Member(memberId), new Category(categoryId))));
  }
}
