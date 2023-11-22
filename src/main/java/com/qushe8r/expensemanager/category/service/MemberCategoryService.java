package com.qushe8r.expensemanager.category.service;

import com.qushe8r.expensemanager.category.dto.GlobalTotalsExpenseResponse;
import com.qushe8r.expensemanager.category.entity.Category;
import com.qushe8r.expensemanager.category.entity.MemberCategory;
import com.qushe8r.expensemanager.category.mapper.MemberCategoryMapper;
import com.qushe8r.expensemanager.category.repository.MemberCategoryQueryDslRepository;
import com.qushe8r.expensemanager.category.repository.MemberCategoryRepository;
import com.qushe8r.expensemanager.common.utils.Validator;
import com.qushe8r.expensemanager.member.entity.Member;
import com.qushe8r.expensemanager.member.entity.MemberDetails;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Transactional(readOnly = true)
@Service
@RequiredArgsConstructor
public class MemberCategoryService {

  private final MemberCategoryMapper memberCategoryMapper;

  private final MemberCategoryRepository memberCategoryRepository;

  private final MemberCategoryQueryDslRepository memberCategoryQueryDslRepository;

  @Transactional
  public MemberCategory findByMemberCategoryOrElseSave(Long memberId, Long categoryId) {
    return memberCategoryRepository
        .findByMemberIdAndCategoryId(memberId, categoryId)
        .orElseGet(
            () ->
                memberCategoryRepository.save(
                    new MemberCategory(new Member(memberId), new Category(categoryId))));
  }

  public GlobalTotalsExpenseResponse getCategorizedExpense(
      MemberDetails memberDetails,
      LocalDate start,
      LocalDate end,
      Long categoryId,
      Long min,
      Long max) {
    Validator.validateDate(start, end);
    Validator.validateAmount(min, max);

    List<MemberCategory> memberCategories =
        memberCategoryQueryDslRepository.query(
            memberDetails.getId(),
            start.atTime(LocalTime.MIN),
            end.atTime(LocalTime.MAX),
            categoryId,
            min,
            max);

    return memberCategoryMapper.toGlobalTotalsExpenseResponse(memberCategories);
  }
}
