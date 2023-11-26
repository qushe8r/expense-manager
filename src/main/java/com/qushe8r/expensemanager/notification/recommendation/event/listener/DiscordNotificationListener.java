package com.qushe8r.expensemanager.notification.recommendation.event.listener;

import com.qushe8r.expensemanager.notification.recommendation.dto.DailyExpenseRecommendationEvent;
import com.qushe8r.expensemanager.notification.recommendation.dto.DailyExpenseRecommendationInformation;
import com.qushe8r.expensemanager.notification.recommendation.dto.discord.DiscordBody;
import com.qushe8r.expensemanager.notification.recommendation.mapper.DiscordMapper;
import java.util.List;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.MediaType;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;

@EnableAsync
@Component
@RequiredArgsConstructor
@Slf4j
public class DiscordNotificationListener implements TodayExpenseRecommendationEventListener {

  @Value("${discord.baseUrl}")
  @Setter
  private String baseUrl;

  private final DiscordMapper discordMapper;

  @Override
  public void handle(DailyExpenseRecommendationEvent event) {
    List<DailyExpenseRecommendationInformation> informations = event.informations();
    DiscordBody discordBody = discordMapper.toDiscordBody(informations);

    WebClient.create(baseUrl)
        .post()
        .uri(event.member().getNotificationUrl().getDiscordUrl())
        .contentType(MediaType.APPLICATION_JSON)
        .bodyValue(discordBody)
        .retrieve()
        .bodyToMono(String.class)
        .subscribe();
  }
}
