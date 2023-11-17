package com.qushe8r.expensemanager.notification.recommendation.event.publisher;

import com.qushe8r.expensemanager.member.entity.Member;
import com.qushe8r.expensemanager.member.repository.MemberRepository;
import com.qushe8r.expensemanager.notification.recommendation.dto.DailyExpenseRecommendationEvent;
import com.qushe8r.expensemanager.notification.recommendation.dto.DailyExpenseRecommendationInformation;
import com.qushe8r.expensemanager.notification.recommendation.repository.DailyExpenseRecommendationInformationRepository;
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
public class DailyExpenseRecommendationPublisher {

  private final MemberRepository memberRepository;

  private final ApplicationEventPublisher applicationEventPublisher;

  private final DailyExpenseRecommendationInformationRepository repository;

  @Scheduled(cron = "0 0 8 * * *")
  public void dailyExpenseRecommendation() {
    YearMonth month = YearMonth.now();
    LocalDateTime start = month.atDay(1).atTime(LocalTime.MIN);
    LocalDateTime end = LocalDateTime.now();

    // @ManyToOne 자동으로 추가 조회 실행되는 것 최적화 해야함
    // NoticifationUrl을 만들고 나면 Member와 함께 조회해서 할 수 있도록 해야함
    List<Member> members = memberRepository.findAll();
    members.forEach(
        member -> {
          List<DailyExpenseRecommendationInformation> informations =
              repository.query(member.getId(), start, end, month);
          applicationEventPublisher.publishEvent(new DailyExpenseRecommendationEvent(informations));
        });
  }
}
