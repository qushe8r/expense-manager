package com.qushe8r.expensemanager.notification.event.publisher;

import com.qushe8r.expensemanager.member.entity.Member;
import com.qushe8r.expensemanager.member.repository.MemberRepository;
import com.qushe8r.expensemanager.notification.recommendation.dto.DailyExpenseRecommendationEvent;
import com.qushe8r.expensemanager.notification.recommendation.dto.DailyExpenseRecommendationInformation;
import com.qushe8r.expensemanager.notification.recommendation.event.publisher.DailyExpenseRecommendationPublisher;
import com.qushe8r.expensemanager.notification.recommendation.repository.DailyExpenseRecommendationInformationRepository;
import java.time.LocalDateTime;
import java.time.YearMonth;
import java.util.List;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.BDDMockito;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.event.ApplicationEvents;
import org.springframework.test.context.event.RecordApplicationEvents;
import org.springframework.test.context.junit.jupiter.SpringExtension;

@ExtendWith(SpringExtension.class)
@RecordApplicationEvents
@Import(DailyExpenseRecommendationPublisher.class)
class DailyExpenseRecommendationPublisherTest {

  @MockBean private MemberRepository memberRepository;

  @MockBean private DailyExpenseRecommendationInformationRepository repository;

  @SuppressWarnings("SpringJavaInjectionPointsAutowiringInspection")
  @Autowired
  private DailyExpenseRecommendationPublisher dailyExpenseRecommendationPublisher;

  @SuppressWarnings("SpringJavaInjectionPointsAutowiringInspection")
  @Autowired
  private ApplicationEvents applicationEvents;

  @Test
  void dailyExpenseRecommendation() {
    // given
    Member member = new Member(1L);

    String category1 = "category1";
    String category2 = "category2";
    Long budgetAmount1 = 1000000L;
    Long budgetAmount2 = 1000000L;
    Long expenseTotals1 = 500000L;
    Long expenseTotals2 = 2000000L;

    DailyExpenseRecommendationInformation information1 =
        new DailyExpenseRecommendationInformation(category1, budgetAmount1, expenseTotals1);
    DailyExpenseRecommendationInformation information2 =
        new DailyExpenseRecommendationInformation(category2, budgetAmount2, expenseTotals2);

    List<DailyExpenseRecommendationInformation> informations = List.of(information1, information2);

    BDDMockito.given(memberRepository.findAllConsentMemberWithNotificationUrls())
        .willReturn(List.of(member));
    BDDMockito.given(
            repository.query(
                Mockito.eq(member.getId()),
                Mockito.any(LocalDateTime.class),
                Mockito.any(LocalDateTime.class),
                Mockito.any(YearMonth.class)))
        .willReturn(informations);

    // when
    dailyExpenseRecommendationPublisher.dailyExpenseRecommendation();

    // then
    long count = applicationEvents.stream(DailyExpenseRecommendationEvent.class).count();

    Assertions.assertThat(count).isEqualTo(1L);
    Mockito.verify(memberRepository, Mockito.times(1)).findAllConsentMemberWithNotificationUrls();
    Mockito.verify(repository, Mockito.times(1))
        .query(
            Mockito.eq(member.getId()),
            Mockito.any(LocalDateTime.class),
            Mockito.any(LocalDateTime.class),
            Mockito.any(YearMonth.class));
  }
}
