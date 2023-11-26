package com.qushe8r.expensemanager.notification.evaluation.event.listener;

import com.qushe8r.expensemanager.notification.evaluation.dto.DailyExpenseEvaluationEvent;
import com.qushe8r.expensemanager.notification.evaluation.dto.DailyExpenseEvaluationInformation;
import com.qushe8r.expensemanager.notification.evaluation.mapper.DailyExpenseEvaluationDiscordMapper;
import com.qushe8r.expensemanager.notification.recommendation.dto.discord.DiscordBody;
import java.util.List;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.event.EventListener;
import org.springframework.http.MediaType;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;

@EnableAsync
@Component
@RequiredArgsConstructor
@Slf4j
public class DailyExpenseEvaluationDiscordListener {

  @Value("${discord.baseUrl}")
  @Setter
  private String baseUrl;

  private final DailyExpenseEvaluationDiscordMapper discordMapper;

  @EventListener
  public void handle(DailyExpenseEvaluationEvent event) {
    List<DailyExpenseEvaluationInformation> informations = event.informations();
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
