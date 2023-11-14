package com.qushe8r.expensemanager.category.service;

import com.qushe8r.expensemanager.category.entity.Category;
import com.qushe8r.expensemanager.category.entity.MemberCategory;
import com.qushe8r.expensemanager.category.repository.MemberCategoryRepository;
import com.qushe8r.expensemanager.matcher.MemberCategoryMatcher;
import com.qushe8r.expensemanager.member.entity.Member;
import java.util.Optional;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.BDDMockito;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class MemberCategoryServiceTest {

  @Mock private MemberCategoryRepository memberCategoryRepository;

  @InjectMocks private MemberCategoryService memberCategoryService;

  @DisplayName("findByMemberCategoryOrElseSave_Find(): memberCategory 조회에 성공하면 반환한다.")
  @Test
  void findByMemberCategoryOrElseSave_Find() {
    // given
    Long memberId = 1L;
    Long categoryId = 3L;

    MemberCategory memberCategory =
        new MemberCategory(1L, new Member(memberId), new Category(categoryId));
    BDDMockito.given(memberCategoryRepository.findByMemberIdAndCategoryId(memberId, categoryId))
        .willReturn(Optional.of(memberCategory));

    // when
    MemberCategory result =
        memberCategoryService.findByMemberCategoryOrElseSave(memberId, categoryId);

    // then
    Assertions.assertThat(result).isEqualTo(memberCategory);
  }

  @DisplayName("findByMemberCategoryOrElseSave_Save(): memberCategory 조회에 실패하면 저장한다.")
  @Test
  void findByMemberCategoryOrElseSave_Save() {
    // given
    Long memberId = 1L;
    Long categoryId = 3L;
    MemberCategory rowMemberCategory =
        new MemberCategory(new Member(memberId), new Category(categoryId));
    MemberCategory memberCategory =
        new MemberCategory(1L, new Member(memberId), new Category(categoryId));

    BDDMockito.given(memberCategoryRepository.findByMemberIdAndCategoryId(memberId, categoryId))
        .willReturn(Optional.empty());

    BDDMockito.given(
            memberCategoryRepository.save(
                Mockito.argThat(new MemberCategoryMatcher(rowMemberCategory))))
        .willReturn(memberCategory);

    // when
    MemberCategory result =
        memberCategoryService.findByMemberCategoryOrElseSave(memberId, categoryId);

    // then
    Assertions.assertThat(result).isEqualTo(memberCategory);
    Mockito.verify(memberCategoryRepository, Mockito.times(1))
        .findByMemberIdAndCategoryId(memberId, categoryId);
    Mockito.verify(memberCategoryRepository, Mockito.times(1))
        .save(Mockito.argThat(new MemberCategoryMatcher(rowMemberCategory)));
  }
}
