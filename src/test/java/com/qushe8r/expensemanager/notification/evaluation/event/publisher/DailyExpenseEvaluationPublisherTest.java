package com.qushe8r.expensemanager.notification.evaluation.event.publisher;

import static org.junit.jupiter.api.Assertions.*;

import com.qushe8r.expensemanager.member.entity.Member;
import com.qushe8r.expensemanager.member.entity.NotificationUrl;
import com.qushe8r.expensemanager.member.repository.MemberRepository;
import com.qushe8r.expensemanager.notification.evaluation.dto.DailyExpenseEvaluationEvent;
import com.qushe8r.expensemanager.notification.evaluation.dto.DailyExpenseEvaluationInformation;
import com.qushe8r.expensemanager.notification.evaluation.repository.DailyExpenseEvaluationInformationRepository;
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
@Import(DailyExpenseEvaluationPublisher.class)
class DailyExpenseEvaluationPublisherTest {

  @MockBean private MemberRepository memberRepository;

  @MockBean private DailyExpenseEvaluationInformationRepository repository;

  @SuppressWarnings("SpringJavaInjectionPointsAutowiringInspection")
  @Autowired
  private DailyExpenseEvaluationPublisher dailyExpenseRecommendationPublisher;

  @SuppressWarnings("SpringJavaInjectionPointsAutowiringInspection")
  @Autowired
  private ApplicationEvents applicationEvents;

  @Test
  void dailyExpenseRecommendation() {
    // given
    NotificationUrl notificationUrl = new NotificationUrl(1L, "/url", null);
    Member member = new Member(1L, "test@email.com", "", true, true, notificationUrl);

    String category1 = "category1";
    String category2 = "category2";
    Long budgetAmount1 = 1000000L;
    Long budgetAmount2 = 1000000L;
    Long expenseTotals1 = 500000L;
    Long expenseTotals2 = 2000000L;
    Long todayTotals1 = 30000L;
    Long todayTotals2 = 150000L;

    DailyExpenseEvaluationInformation information1 =
        new DailyExpenseEvaluationInformation(
            category1, expenseTotals1, todayTotals1, budgetAmount1);

    DailyExpenseEvaluationInformation information2 =
        new DailyExpenseEvaluationInformation(
            category2, expenseTotals2, todayTotals2, budgetAmount2);

    List<DailyExpenseEvaluationInformation> informations = List.of(information1, information2);

    BDDMockito.given(memberRepository.findAllConsentMemberWithNotificationUrls())
        .willReturn(List.of(member));
    BDDMockito.given(
            repository.query(
                Mockito.any(LocalDateTime.class),
                Mockito.any(LocalDateTime.class),
                Mockito.any(LocalDateTime.class),
                Mockito.any(YearMonth.class),
                Mockito.eq(member.getId())))
        .willReturn(informations);

    // when
    dailyExpenseRecommendationPublisher.dailyExpenseRecommendation();

    // then
    long count = applicationEvents.stream(DailyExpenseEvaluationEvent.class).count();

    Assertions.assertThat(count).isEqualTo(1L);
    Mockito.verify(memberRepository, Mockito.times(1)).findAllConsentMemberWithNotificationUrls();
    Mockito.verify(repository, Mockito.times(1))
        .query(
            Mockito.any(LocalDateTime.class),
            Mockito.any(LocalDateTime.class),
            Mockito.any(LocalDateTime.class),
            Mockito.any(YearMonth.class),
            Mockito.eq(member.getId()));
  }
}
