package com.qushe8r.expensemanager.notification.evaluation.event.listener;

import com.qushe8r.expensemanager.notification.evaluation.dto.DailyExpenseEvaluationEvent;
import com.qushe8r.expensemanager.notification.evaluation.dto.DailyExpenseEvaluationInformation;
import com.qushe8r.expensemanager.notification.evaluation.mapper.DailyExpenseEvaluationDiscordMapper;
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
class DailyExpenseEvaluationDiscordListenerTest {

  private static MockWebServer mockWebServer;

  @Spy private DailyExpenseEvaluationDiscordMapper discordMapper;

  @InjectMocks private DailyExpenseEvaluationDiscordListener discordNotificationListener;

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

    DailyExpenseEvaluationEvent dailyExpenseRecommendationEvent =
        new DailyExpenseEvaluationEvent(informations);

    discordNotificationListener.setBaseUrl(baseUrl);
    discordNotificationListener.setUri(url);

    // when
    discordNotificationListener.handle(dailyExpenseRecommendationEvent);

    // then
    RecordedRequest recordedRequest = mockWebServer.takeRequest();
    Assertions.assertThat(recordedRequest.getMethod()).isEqualTo("POST");
    Assertions.assertThat(recordedRequest.getPath()).isEqualTo(url);
  }
}
