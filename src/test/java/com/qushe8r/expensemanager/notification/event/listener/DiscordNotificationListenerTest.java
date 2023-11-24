package com.qushe8r.expensemanager.notification.event.listener;

import com.qushe8r.expensemanager.member.entity.Member;
import com.qushe8r.expensemanager.member.entity.NotificationUrl;
import com.qushe8r.expensemanager.notification.recommendation.dto.DailyExpenseRecommendationEvent;
import com.qushe8r.expensemanager.notification.recommendation.dto.DailyExpenseRecommendationInformation;
import com.qushe8r.expensemanager.notification.recommendation.event.listener.DiscordNotificationListener;
import com.qushe8r.expensemanager.notification.recommendation.mapper.DiscordMapper;
import java.io.IOException;
import java.util.List;
import okhttp3.mockwebserver.MockWebServer;
import okhttp3.mockwebserver.RecordedRequest;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class DiscordNotificationListenerTest {

  private static MockWebServer mockWebServer;

  @Spy private DiscordMapper discordMapper;

  @InjectMocks private DiscordNotificationListener discordNotificationListener;

  private static String baseUrl;

  @BeforeAll
  static void setUp() throws IOException {
    mockWebServer = new MockWebServer();
    mockWebServer.start();
    baseUrl = mockWebServer.url("").toString();
  }

  @AfterAll
  static void tearDown() throws IOException {
    mockWebServer.shutdown();
  }

  @Test
  void handle() throws InterruptedException {
    // given
    System.out.println("CurrentThread: " + Thread.currentThread().getName());
    String url = "/";

    NotificationUrl notificationUrl = new NotificationUrl(1L, url, null);
    Member member = new Member(1L, "test@email.com", "", true, true, notificationUrl);

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

    DailyExpenseRecommendationEvent dailyExpenseRecommendationEvent =
        new DailyExpenseRecommendationEvent(member, informations);

    discordNotificationListener.setBaseUrl(baseUrl);

    // when
    discordNotificationListener.handle(dailyExpenseRecommendationEvent);

    // then
    RecordedRequest recordedRequest = mockWebServer.takeRequest();
    Assertions.assertThat(recordedRequest.getMethod()).isEqualTo("POST");
    Assertions.assertThat(recordedRequest.getPath()).isEqualTo(url);
  }
}
