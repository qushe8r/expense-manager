package com.qushe8r.expensemanager.notification.evaluation.event.publisher;

import com.qushe8r.expensemanager.member.entity.Member;
import com.qushe8r.expensemanager.member.repository.MemberRepository;
import com.qushe8r.expensemanager.notification.evaluation.dto.DailyExpenseEvaluationEvent;
import com.qushe8r.expensemanager.notification.evaluation.dto.DailyExpenseEvaluationInformation;
import com.qushe8r.expensemanager.notification.evaluation.repository.DailyExpenseEvaluationInformationRepository;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.YearMonth;
import java.util.List;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
@Slf4j
public class DailyExpenseEvaluationPublisher {

  private final MemberRepository memberRepository;

  private final ApplicationEventPublisher applicationEventPublisher;

  private final DailyExpenseEvaluationInformationRepository repository;

  @Scheduled(cron = "0 0 20 * * *")
  public void dailyExpenseRecommendation() {
    YearMonth month = YearMonth.now();
    LocalDateTime firstDay = month.atDay(1).atTime(LocalTime.MIN);
    LocalDateTime todayStart = LocalDate.now().atTime(LocalTime.MIN);
    LocalDateTime now = LocalDateTime.now();

    // @ManyToOne 자동으로 추가 조회 실행되는 것 최적화 해야함
    // NoticifationUrl을 만들고 나면 Member와 함께 조회해서 할 수 있도록 해야함
    List<Member> members = memberRepository.findAllConsentMemberWithNotificationUrls();
    members.forEach(
        member -> {
          List<DailyExpenseEvaluationInformation> informations =
              repository.query(firstDay, todayStart, now, month, member.getId());
          applicationEventPublisher.publishEvent(
              new DailyExpenseEvaluationEvent(member, informations));
        });
  }
}
