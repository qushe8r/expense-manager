package com.qushe8r.expensemanager.notification.recommendation.mapper;

import com.qushe8r.expensemanager.notification.recommendation.dto.DailyExpenseRecommendationInformation;
import com.qushe8r.expensemanager.notification.recommendation.dto.discord.DiscordBody;
import com.qushe8r.expensemanager.notification.recommendation.dto.discord.DiscordEmbed;
import com.qushe8r.expensemanager.notification.recommendation.dto.discord.DiscordEmbedAuthor;
import com.qushe8r.expensemanager.notification.recommendation.dto.discord.DiscordEmbedField;
import com.qushe8r.expensemanager.notification.recommendation.dto.discord.DiscordEmbedFooter;
import com.qushe8r.expensemanager.notification.recommendation.dto.discord.DiscordEmbedThumbnail;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class DiscordMapper {

  @Value("${discord.username}")
  private String username;

  @Value("${discord.avatarUrl}")
  private String avatarUrl;

  @Value("${discord.content}")
  private String content;

  @Value("${discord.author.icon}")
  private String authorIconUrl;

  @Value("${discord.thumbnail}")
  private String thumbnail;

  @Value("${discord.color}")
  private Integer color;

  @Value("${discord.total.name}")
  private String totalCategoryName;

  @Value("${discord.footer.text}")
  private String text;

  @Value("${discord.phrase.red}")
  private String phraseRed;

  @Value("${discord.phrase.green}")
  private String phraseGreen;

  public DiscordBody toDiscordBody(List<DailyExpenseRecommendationInformation> informations) {
    return new DiscordBody(username, avatarUrl, content, toDiscordEmbed(informations));
  }

  private List<DiscordEmbed> toDiscordEmbed(
      List<DailyExpenseRecommendationInformation> informations) {
    return List.of(
        new DiscordEmbed(
            createDiscordEmbedAuthor(),
            new DiscordEmbedThumbnail(thumbnail),
            color,
            toDiscordEmbedFields(informations),
            new DiscordEmbedFooter(text)));
  }

  private DiscordEmbedAuthor createDiscordEmbedAuthor() {
    String today = LocalDate.now().format(DateTimeFormatter.ofPattern("yyyy년 MM월 dd일"));
    String name = today + "\n오늘의 지출 추천 금액은?";
    return new DiscordEmbedAuthor(name, authorIconUrl);
  }

  private List<DiscordEmbedField> toDiscordEmbedFields(
      List<DailyExpenseRecommendationInformation> informations) {
    List<DiscordEmbedField> fields = new ArrayList<>();
    fields.add(toGlobalToTalsDiscordEmbedField(informations));
    fields.addAll(informations.stream().map(this::toDiscordEmbedField).toList());
    return fields;
  }

  private DiscordEmbedField toGlobalToTalsDiscordEmbedField(
      List<DailyExpenseRecommendationInformation> informations) {
    long globalBudgetTotals =
        informations.stream().mapToLong(DailyExpenseRecommendationInformation::budgeAmount).sum();

    Long globalExpenseTotals =
        informations.stream().mapToLong(DailyExpenseRecommendationInformation::expenseTotals).sum();

    return toDiscordEmbedField(
        new DailyExpenseRecommendationInformation(
            totalCategoryName, globalBudgetTotals, globalExpenseTotals));
  }

  public DiscordEmbedField toDiscordEmbedField(DailyExpenseRecommendationInformation information) {
    Calendar today = Calendar.getInstance();
    int date = today.get(Calendar.DATE);
    int dayOfMonth = today.getActualMaximum(Calendar.DAY_OF_MONTH);

    double currentMonthProgress = date / (double) dayOfMonth;
    Long budgetAmount = information.budgeAmount();
    Long expenseTotals = information.expenseTotals();
    String assessmentPhrase = assessmentPhrase(currentMonthProgress, budgetAmount, expenseTotals);
    Long recommendationAmount =
        budgetAmount - expenseTotals / ((dayOfMonth - date) * 1000L) * 1000L;

    StringBuilder value = new StringBuilder();
    value
        .append(assessmentPhrase)
        .append("\n예산: ")
        .append(budgetAmount)
        .append("\n지출: ")
        .append(expenseTotals)
        .append("\n금일 지출 추천 금액: ")
        .append(recommendationAmount);

    return new DiscordEmbedField(information.categoryName(), value.toString());
  }

  private String assessmentPhrase(
      Double currentMonthProgress, Long budgetAmount, Long expenseTotals) {
    if (evaluateExpenseManagement(currentMonthProgress, budgetAmount, expenseTotals)) {
      return phraseGreen;
    }
    return phraseRed;
  }

  private boolean evaluateExpenseManagement(
      Double currentMonthProgress, Long budgetAmount, Long expenseAmount) {
    double expenseToBudgetRatio = expenseAmount / budgetAmount.doubleValue();
    return currentMonthProgress >= expenseToBudgetRatio;
  }
}
